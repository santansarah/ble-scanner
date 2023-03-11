package com.santansarah.scan.domain.usecases

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.santansarah.scan.domain.models.DeviceService
import com.santansarah.scan.domain.models.updateBytes
import timber.log.Timber

class ParseRead() {

    operator fun invoke(
        deviceDetails: List<DeviceService>,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ): List<DeviceService> {

        if (status == BluetoothGatt.GATT_SUCCESS) {

            val newList = deviceDetails.map { svc ->
                svc.copy(characteristics =
                svc.characteristics.map { char ->
                    if (char.uuid == characteristic.uuid.toString()) {
                        char.updateBytes(characteristic.value)
                    } else
                        char
                })
            }

            Timber.d("newList: $newList")

            return newList
        } else {
            return deviceDetails
        }

    }
}
