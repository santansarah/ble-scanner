package com.santansarah.blescanner.domain.usecases

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.Service
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDescriptor
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.utils.BlePermissions
import com.santansarah.blescanner.utils.BleProperties
import com.santansarah.blescanner.utils.BleWriteTypes
import com.santansarah.blescanner.utils.canRead
import com.santansarah.blescanner.utils.canWrite
import com.santansarah.blescanner.utils.toGss
import timber.log.Timber
import java.util.UUID

class ParseService
    (
    private val bleRepository: BleRepository
) {

    @SuppressLint("MissingPermission")
    suspend operator fun invoke(gatt: BluetoothGatt, status: Int):
            List<DeviceService> {

        val services = mutableListOf<DeviceService>()

        if (status == BluetoothGatt.GATT_SUCCESS) {
            gatt.services?.forEach { gattService ->

                val serviceName =
                    bleRepository.getServiceById(gattService.uuid.toGss())?.name ?: "Mfr Service"

                val service = Service(
                    "",
                    serviceName,
                    "",
                    gattService.uuid.toString().uppercase()
                )

                val characteristics = mutableListOf<DeviceCharacteristics>()

                gattService.characteristics.forEach { char ->
                    val deviceCharacteristic = bleRepository
                        .getCharacteristicById(char.uuid.toGss())

                    val permissions = char.permissions
                    val properties = BleProperties.getAllProperties(char.properties)
                    val writeTypes = BleWriteTypes.getAllTypes(char.writeType)

                    val descriptors = mutableListOf<DeviceDescriptor>()
                    char.descriptors?.forEach { desc ->

                        if (descriptors.find { makeSureNotDup ->
                                makeSureNotDup.uuid == desc.uuid.toString()} == null) {

                            Timber.d(char.uuid.toString())
                            Timber.d(desc.uuid.toString() + "; " + desc.characteristic.uuid.toString())

                            val deviceDescriptor = bleRepository.getDescriptorById(
                                desc.uuid.toGss()
                            )

                            descriptors.add(
                                DeviceDescriptor(
                                    desc.uuid.toString(),
                                    deviceDescriptor?.name ?: "Unknown",
                                    BlePermissions.getAllPermissions(desc.permissions),
                                    null
                                )
                            )
                        }
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
                            canRead = properties.canRead(),
                            canWrite = properties.canWrite(),
                            readBytes = null,
                            notificationBytes = null
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
