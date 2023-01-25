package com.santansarah.blescanner.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import android.util.SparseArray
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.utils.toGss
import com.santansarah.blescanner.utils.toHex
import com.santansarah.blescanner.utils.toMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber

class BleManager(
    app: Application,
    private val bleRepository: BleRepository,
    private val scope: CoroutineScope
): KoinComponent {

    var isScanning = false

    private val btAdapter: BluetoothAdapter = get()
    private val btScanner = btAdapter.bluetoothLeScanner

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val scanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {

            scope.launch {
                Timber.d("device: $result")

                var mfName: String? = null
                var services: List<String>? = null
                var extra: List<String>? = null

                result.scanRecord?.manufacturerSpecificData?.let { mfData ->
                    getMfId(mfData)?.let { mfId ->
                        mfName = getMfName(mfId)

                        result.scanRecord?.getManufacturerSpecificData(mfId)?.let { mfBytes ->
                            if (mfId == 6) {
                                getMsDevice(mfBytes)?.let {msDevice ->
                                    extra = listOf(msDevice)
                                }
                            }
                        }
                    }
                }

                result.scanRecord?.serviceUuids?.let {
                    services = getServices(it)
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

                val recNum = bleRepository.insertDevice(device)
                Timber.d("$recNum: ${device.toString()}")

            }

        }

    }

    fun getMfId(
        mfData: SparseArray<ByteArray>
    ): Int? {

        var mfId: Int? = null
        for (i in 0 until mfData.size()) {
            mfId = mfData.keyAt(i)
        }
        return mfId
    }

    suspend fun getMfName(
        mfId: Int
    ): String? = bleRepository.getCompanyById(mfId)?.name

    suspend fun getMsDevice(
        byteArray: ByteArray
    ): String? {
        val msDeviceType = byteArray[1].toHex().toInt()
        return bleRepository.getMicrosoftDeviceById(msDeviceType)?.name
    }

    suspend fun getServices(
        serviceIdRecord: List<ParcelUuid>
    ): List<String>? {
        var serviceNames: MutableList<String>? = null

        serviceIdRecord.forEach { serviceId ->
            val formattedId = serviceId.uuid.toGss()
            bleRepository.getServiceById(formattedId)?.name?.let { serviceName ->
                if (serviceNames == null)
                    serviceNames = mutableListOf()
                serviceNames?.add(serviceName)
            }
        }

        return serviceNames?.toList()

    }

    init {
        scope.launch {
            bleRepository.deleteScans()
            //scan()
        }
    }

    @SuppressLint("MissingPermission")
    fun scan() {
        isScanning = true
        btScanner.startScan(null,scanSettings,scanCallback)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        isScanning = false
        btScanner.stopScan(scanCallback)
    }

}