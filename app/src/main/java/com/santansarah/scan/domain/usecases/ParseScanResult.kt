package com.santansarah.scan.domain.usecases

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.util.SparseArray
import com.santansarah.scan.domain.interfaces.IBleRepository
import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.utils.toMillis
import timber.log.Timber

class ParseScanResult
    (
    private val bleRepository: IBleRepository
) {

    @SuppressLint("MissingPermission")
    suspend operator fun invoke(result: ScanResult) {

        try {

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

            val device = ScannedDevice(
                deviceId = 0,
                deviceName = result.device.name,
                address = result.device.address,
                rssi = result.rssi,
                manufacturer = mfName,
                services = services,
                extra = extra,
                lastSeen = result.timestampNanos.toMillis(),
                customName = null,
                baseRssi = 0,
                favorite = false,
                forget = false
            )

            val recNum = bleRepository.insertDevice(device)
        } catch (e: Exception) {
            Timber.e(e, "Insert Device")
        }

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