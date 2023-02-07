package com.santansarah.blescanner.domain.usecases

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.util.SparseArray
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.utils.toMillis
import timber.log.Timber

class ParseScanResult
    (
    private val bleRepository: BleRepository
) {

    @SuppressLint("MissingPermission")
    suspend operator fun invoke(result: ScanResult) {

        Timber.d("device: $result")

        var mfName: String? = null
        var services: List<String>? = null
        var extra: List<String>? = null

        result.scanRecord?.manufacturerSpecificData?.let { mfData ->
            getMfId(mfData)?.let { mfId ->
                mfName = bleRepository.getCompanyById(mfId)?.name

                result.scanRecord?.getManufacturerSpecificData(mfId)?.let { mfBytes ->
                    if (mfId == 6) {
                        bleRepository.getMsDevice(mfBytes)?.let { msDevice ->
                            extra = listOf(msDevice)
                        }
                    }
                }
            }
        }

        result.scanRecord?.serviceUuids?.let {
            services = bleRepository.getServices(it)
        }

        var device = ScannedDevice(
            deviceId = 0,
            deviceName = result.device.name,
            address = result.device.address,
            rssi = result.rssi,
            manufacturer = mfName,
            services = services,
            extra = extra,
            lastSeen = result.timestampNanos.toMillis()
        )

        if (device.manufacturer != "Microsoft") {
            val recNum = bleRepository.insertDevice(device)
        }
        Timber.d("$device")

    }

    private fun getMfId(
        mfData: SparseArray<ByteArray>
    ): Int? {

        var mfId: Int? = null
        for (i in 0 until mfData.size()) {
            mfId = mfData.keyAt(i)
        }
        return mfId
    }

}