package com.santansarah.blescanner.domain.usecases

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.domain.models.updateDescriptors
import timber.log.Timber

class ParseDescriptor() {

    operator fun invoke(
        deviceDetails: List<DeviceService>,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ): List<DeviceService> {

        if (status == BluetoothGatt.GATT_SUCCESS) {

            Timber.d(descriptor.value.toString())

            val newList = deviceDetails.map { dd ->
                dd.copy(characteristics =
                dd.characteristics.map { char ->
                    if (descriptor.characteristic.uuid.toString() == char.uuid) {
                        char.copy(
                            descriptors =
                            char.updateDescriptors(descriptor.uuid.toString(), descriptor.value)
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
