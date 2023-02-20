package com.santansarah.blescanner.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.os.Build
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.domain.bleparsables.CCCD
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.domain.usecases.ParseDescriptor
import com.santansarah.blescanner.domain.usecases.ParseNotification
import com.santansarah.blescanner.domain.usecases.ParseRead
import com.santansarah.blescanner.domain.usecases.ParseService
import com.santansarah.blescanner.utils.print
import com.santansarah.blescanner.utils.toHex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber
import java.util.UUID

@SuppressLint("MissingPermission")
class BleGatt(
    private val app: Application,
    private val scope: CoroutineScope,
    private val parseService: ParseService,
    private val parseRead: ParseRead,
    private val parseNotification: ParseNotification,
    private val parseDescriptor: ParseDescriptor
) : KoinComponent {

    private var btGatt: BluetoothGatt? = null
    private val btAdapter: BluetoothAdapter = get()

    val connectMessage = MutableStateFlow(ConnectionState.DISCONNECTED)
    val deviceDetails = MutableStateFlow<List<DeviceService>>(emptyList())

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            btGatt = gatt
            Timber.d("status: $status")

            when (newState) {
                BluetoothProfile.STATE_CONNECTING -> connectMessage.value =
                    ConnectionState.CONNECTING

                BluetoothProfile.STATE_CONNECTED -> {
                    connectMessage.value = ConnectionState.CONNECTED
                    btGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTING -> connectMessage.value =
                    ConnectionState.DISCONNECTING

                BluetoothProfile.STATE_DISCONNECTED -> connectMessage.value =
                    ConnectionState.DISCONNECTED

                else -> connectMessage.value = ConnectionState.DISCONNECTED
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            scope.launch {
                deviceDetails.value = emptyList()
                gatt?.let {
                    deviceDetails.value = parseService(it, status)
                    enableNotificationsAndIndications()
                }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            deviceDetails.value = parseRead(deviceDetails.value, characteristic, status)
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            deviceDetails.value = parseRead(deviceDetails.value, characteristic, status)
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            Timber.d("characteristic changed: ${characteristic.value.print()}")
            deviceDetails.value = parseNotification(deviceDetails.value, characteristic)
        }

        @Deprecated("Deprecated in Java")
        override fun onDescriptorRead(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)

            Timber.d(
                "descriptor read: ${descriptor.uuid}, " +
                        "${descriptor.characteristic.uuid}, $status, ${descriptor.value.print()}"
            )

            deviceDetails.value = parseDescriptor(deviceDetails.value, descriptor, status)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            btGatt?.readCharacteristic(characteristic)
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Timber.d("descriptor write: ${descriptor.uuid}, ${descriptor.characteristic.uuid}, $status")
        }

    }

    suspend fun enableNotificationsAndIndications() {

        btGatt?.services?.forEach { gattSvcForNotify ->
            gattSvcForNotify.characteristics?.forEach { svcChar ->

                svcChar.descriptors.find { desc ->
                    desc.uuid.toString() == CCCD.uuid
                }?.also { cccd ->
                    val notifyRegistered = btGatt?.setCharacteristicNotification(svcChar, true)

                    if (svcChar.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                        cccd.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                        btGatt?.writeDescriptor(cccd)
                    }

                    if (svcChar.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE > 0) {
                        cccd.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE)
                        btGatt?.writeDescriptor(cccd)
                    }

                    // give gatt a little breathing room for writes
                    delay(300L)

                }

            }
        }
    }

    fun connect(address: String) {
        btAdapter.let { adapter ->
            try {
                connectMessage.value = ConnectionState.CONNECTING
                val device = adapter.getRemoteDevice(address)
                device.connectGatt(app, false, bluetoothGattCallback)
            } catch (exception: IllegalArgumentException) {
                Timber.w("Device not found with provided address.")
            }
        }
    }

    fun readCharacteristic(uuid: String) {
        btGatt?.services?.flatMap { it.characteristics }?.find { svcChar ->
            svcChar.uuid.toString() == uuid
        }?.also { foundChar ->
            Timber.d("Found Char: " + foundChar.uuid.toString())
            btGatt?.readCharacteristic(foundChar)
        }
    }

    fun readDescriptor(charUuid: String, descUuid: String) {

        val currentCharacteristic = btGatt?.services?.flatMap { it.characteristics }?.find { char ->
            char.uuid.toString() == charUuid
        }
        currentCharacteristic?.let { char ->
            char.descriptors.find { desc ->
                desc.uuid.toString() == descUuid
            }?.also { foundDesc ->
                Timber.d("Found Char: $charUuid; " + foundDesc.uuid.toString())
                btGatt?.readDescriptor(foundDesc)
            }
        }

    }

    fun writeBytes(uuid: String, bytes: ByteArray) {
        btGatt?.services?.flatMap { it.characteristics }?.find { svcChar ->
            svcChar.uuid.toString() == uuid
        }?.also { foundChar ->
            Timber.d("Found Char: " + foundChar.uuid.toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                btGatt?.writeCharacteristic(
                    foundChar,
                    bytes,
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
            } else {
                foundChar.setValue(bytes)
                btGatt?.writeCharacteristic(foundChar)
            }
        }

    }

    fun writeDescriptor(charUuid: String, uuid: String, bytes: ByteArray) {
        btGatt?.services?.flatMap { it.characteristics }?.flatMap { it.descriptors }
            ?.find { it.characteristic.uuid.toString() == charUuid && it.uuid.toString() == uuid }
            ?.also { foundDescriptor ->
                foundDescriptor.setValue(bytes)
                btGatt?.writeDescriptor(foundDescriptor)
            }
    }

    fun close() {
        connectMessage.value = ConnectionState.DISCONNECTED
        deviceDetails.value = emptyList()
        btGatt?.let { gatt ->
            gatt.disconnect()
            gatt.close()
            btGatt = null
        }
    }


}