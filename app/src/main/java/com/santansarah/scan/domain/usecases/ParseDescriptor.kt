package com.santansarah.scan.domain.usecases

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.santansarah.scan.domain.models.DeviceService
import com.santansarah.scan.domain.models.updateDescriptors
import timber.log.Timber

class ParseDescriptor() {

    operator fun invoke(
        deviceDetails: List<DeviceService>,
        descriptor: BluetoothGattDescriptor,
        status: Int,
        value: ByteArray
    ): List<DeviceService> {

        if (status == BluetoothGatt.GATT_SUCCESS) {

            Timber.d(value.toString())

            val newList = deviceDetails.map { dd ->
                dd.copy(characteristics =
                dd.characteristics.map { char ->
                    if (descriptor.characteristic.uuid.toString() == char.uuid) {
                        char.copy(
                            descriptors =
                            char.updateDescriptors(descriptor.uuid.toString(), value)
                        )
                    }
                    else
                        char
                })
            }

            Timber.d("newDescriptorList: $newList")

            return newList
        } else {
            return deviceDetails
        }

    }
}
