package com.santansarah.blescanner.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.Service
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDescriptor
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.utils.decodeSkipUnreadable
import com.santansarah.blescanner.utils.toGss
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber

@SuppressLint("MissingPermission")
class BleGatt(
    private val app: Application,
    private val bleRepository: BleRepository,
    private val scope: CoroutineScope
) : KoinComponent {

    private var btGatt: BluetoothGatt? = null
    private val btAdapter: BluetoothAdapter = get()

    val connectMessage = MutableStateFlow(ConnectionState.DISCONNECTED)
    val deviceDetails = MutableStateFlow<List<DeviceService>>(emptyList())

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {

            btGatt = gatt
            Timber.d("status: $status")

            when (newState) {
                BluetoothProfile.STATE_CONNECTING -> connectMessage.value = ConnectionState.CONNECTING
                BluetoothProfile.STATE_CONNECTED -> {
                    connectMessage.value = ConnectionState.CONNECTED
                    btGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTING -> connectMessage.value = ConnectionState.DISCONNECTING
                BluetoothProfile.STATE_DISCONNECTED -> connectMessage.value = ConnectionState.DISCONNECTED
                else -> connectMessage.value = ConnectionState.DISCONNECTED
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {

            scope.launch {

                deviceDetails.value = emptyList()
                val services = mutableListOf<DeviceService>()

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    gatt?.services?.forEach { gatt ->

                        val service = bleRepository.getServiceById(gatt.uuid.toGss()) ?: Service(
                            "",
                            "Mfr Service",
                            "",
                            gatt.uuid.toString()
                        )

                        val characteristics = mutableListOf<DeviceCharacteristics>()
                        val descriptors = mutableListOf<DeviceDescriptor>()

                        gatt.characteristics.forEach { char ->
                            val deviceCharacteristic = bleRepository
                                .getCharacteristicById(char.uuid.toGss())

                            val permissions = char.permissions
                            val properties = char.properties
                            val writeTypes = char.writeType

                            char.descriptors.forEach { desc ->
                                descriptors.add(
                                    DeviceDescriptor(
                                        desc.uuid.toString(),
                                        desc.permissions
                                    )
                                )
                            }

                            characteristics.add(
                                DeviceCharacteristics(
                                    uuid = char.uuid.toString(),
                                    name = deviceCharacteristic?.name ?: "Mfr Characteristic",
                                    descriptor = null,
                                    permissions = permissions,
                                    properties = properties,
                                    writeTypes = writeTypes,
                                    descriptors = descriptors,
                                    canRead = char.properties and BluetoothGattCharacteristic.PROPERTY_READ > 0,
                                    canWrite = char.properties
                                            and (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE or
                                            BluetoothGattCharacteristic.PROPERTY_WRITE or
                                            BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE) > 0,
                                    readBytes = null
                                )
                            )
                        }

                        val deviceService = DeviceService(
                            service.uuid,
                            service.name,
                            characteristics
                        )

                        services.add(deviceService)

                        Timber.d(services.toString())

                        deviceDetails.value = services.toList()

                    }
                } else {
                    Timber.w("onServicesDiscovered received: $status")
                }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

                Timber.d(status.toString())
                Timber.d("Read: ${characteristic.value.decodeSkipUnreadable()}")
                Timber.d("Read: ${characteristic.uuid}")

                val readValue = characteristic.value.decodeSkipUnreadable()

                val newList = deviceDetails.value.map { svc ->
                    svc.copy(characteristics =
                    svc.characteristics.map { char ->
                        if (char.uuid == characteristic.uuid.toString()) {
                            char.updateBytes(characteristic.value)
                        }
                        else
                            char
                    })
                }

                Timber.d(newList.toString())

                deviceDetails.value = newList
            }

        }
    }

    fun connect(address: String) {
        btAdapter.let { adapter ->
            try {
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