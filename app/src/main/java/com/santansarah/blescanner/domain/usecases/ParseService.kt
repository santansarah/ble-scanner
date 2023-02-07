package com.santansarah.blescanner.domain.usecases

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import android.util.SparseArray
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.Service
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDescriptor
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.utils.toGss
import com.santansarah.blescanner.utils.toMillis
import kotlinx.coroutines.launch
import timber.log.Timber

class ParseService
    (
    private val bleRepository: BleRepository
) {

    @SuppressLint("MissingPermission")
    suspend operator fun invoke(gattService: List<BluetoothGattService>?, status: Int):
            List<DeviceService> {

        val services = mutableListOf<DeviceService>()

        if (status == BluetoothGatt.GATT_SUCCESS) {
            gattService?.forEach { gatt ->

                val serviceName =
                    bleRepository.getServiceById(gatt.uuid.toGss())?.name ?: "Mfr Service"

                val service = Service(
                    "",
                    serviceName,
                    "",
                    gatt.uuid.toString().uppercase()
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

            }
        }

        return services.toList()

    }

}
