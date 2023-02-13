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
import com.santansarah.blescanner.domain.models.updateBytes
import com.santansarah.blescanner.utils.decodeSkipUnreadable
import com.santansarah.blescanner.utils.toGss
import com.santansarah.blescanner.utils.toMillis
import kotlinx.coroutines.launch
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
