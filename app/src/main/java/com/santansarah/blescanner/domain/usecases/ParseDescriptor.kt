package com.santansarah.blescanner.domain.usecases

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import android.util.SparseArray
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.Service
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDescriptor
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.domain.models.updateDescriptors
import com.santansarah.blescanner.utils.decodeSkipUnreadable
import com.santansarah.blescanner.utils.toGss
import com.santansarah.blescanner.utils.toMillis
import kotlinx.coroutines.launch
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
