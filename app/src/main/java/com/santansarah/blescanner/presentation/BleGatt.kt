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
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.domain.usecases.ParseDescriptor
import com.santansarah.blescanner.domain.usecases.ParseNotification
import com.santansarah.blescanner.domain.usecases.ParseRead
import com.santansarah.blescanner.domain.usecases.ParseService
import com.santansarah.blescanner.utils.ParsableCharacteristic
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

            with(characteristic) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        Timber.i(
                            "BluetoothGattCallback",
                            "Wrote to characteristic $uuid | value: ${value.toHex()}"
                        )
                    }

                    BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH -> {
                        Timber.e(
                            "BluetoothGattCallback",
                            "Write exceeded connection ATT MTU!"
                        )
                    }

                    BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
                        Timber.e(
                            "BluetoothGattCallback",
                            "Write not permitted for $uuid!"
                        )
                    }

                    else -> {
                        Timber.e(
                            "BluetoothGattCallback",
                            "Characteristic write failed for $uuid, error: $status"
                        )
                    }
                }
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)

            Timber.d("descriptor write: ${descriptor.uuid}, ${descriptor.characteristic.uuid}, $status")

            with(descriptor) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        //btGatt?.readDescriptor(descriptor)
                    }

                    BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH -> {
                        Timber.e(
                            "BluetoothGattCallback",
                            "Write exceeded connection ATT MTU!"
                        )
                    }

                    BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
                        Timber.e(
                            "BluetoothGattCallback",
                            "Write not permitted for $uuid!"
                        )
                    }

                    else -> {
                        Timber.e(
                            "BluetoothGattCallback",
                            "descriptor write failed for $uuid, error: $status"
                        )
                    }
                }
            }
        }

    }

    suspend fun enableNotificationsAndIndications() {

/*
        val btService = btGatt?.getService(UUID.fromString("00000af0$UUID_DEFAULT".lowercase()))
        val btChar =
            btService?.getCharacteristic(UUID.fromString("0000af7$UUID_DEFAULT".lowercase()))
        val btDesc = btChar?.getDescriptor(UUID.fromString("00002902$UUID_DEFAULT"))

        val registered = btGatt?.setCharacteristicNotification(btChar, true)
        Timber.d("${btChar?.uuid} registered: $registered")

        btDesc?.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        btGatt?.writeDescriptor(btDesc)
*/

        btGatt?.services?.forEach { gattSvcForNotify ->
            gattSvcForNotify.characteristics?.forEach { svcChar ->

                if (svcChar.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                    val notifyRegistered = btGatt?.setCharacteristicNotification(svcChar, true)
                    Timber.d("${svcChar.uuid} registered: $notifyRegistered")

                    val notifyDescriptor = svcChar.getDescriptor(
                        UUID.fromString(ParsableCharacteristic.CCCD.uuid.lowercase()))
                    notifyDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                    btGatt?.writeDescriptor(notifyDescriptor)
                }

                if (svcChar.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE > 0) {
                    val indicateRegistered = btGatt?.setCharacteristicNotification(svcChar, true)
                    Timber.d("${svcChar.uuid} registered: $indicateRegistered")

                    val indicateDescriptor = svcChar.getDescriptor(
                        UUID.fromString(ParsableCharacteristic.CCCD.uuid.lowercase()))
                    indicateDescriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE)
                    btGatt?.writeDescriptor(indicateDescriptor)
                }

                // give the gatt a little breathing room for writes
                delay(300L)
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