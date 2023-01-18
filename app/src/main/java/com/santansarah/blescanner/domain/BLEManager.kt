package com.santansarah.blescanner.domain

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.SparseArray
import androidx.activity.result.ActivityResultLauncher
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.utils.toGss
import com.santansarah.blescanner.utils.toHex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes

class BLEManager(
    app: Application,
    private val bleRepository: BleRepository,
    private val scope: CoroutineScope
): KoinComponent {

    var isScanning = false

    private val btAdapter: BluetoothAdapter = get()
    private val btScanner = btAdapter.bluetoothLeScanner

    private lateinit var btEnableResultLauncher: ActivityResultLauncher<Intent>

    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())

    private val SCAN_PERIOD: Long = 4.minutes.inWholeMilliseconds

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
                    deviceName = result.device.name,
                    address = result.device.address,
                    rssi = result.rssi,
                    manufacturer = mfName,
                    services = services,
                    extra = extra
                )

                bleRepository.insertDevice(device)

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
        setUpLauncher()
        checkEnabled()
        scan()
    }

    private fun setUpLauncher() {
        /*btEnableResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
            }
        }*/
    }

    private fun checkEnabled() {
        /*if (!btAdapter.isEnabled) {
            val btEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            btEnableResultLauncher.launch(btEnableIntent)
        }*/
    }

    /*@SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        scope.launch {
            if (!scanning) { // Stops scanning after a pre-defined scan period.
                handler.postDelayed({
                    scanning = false
                    Timber.d("stopped scanning...")
                    btScanner.stopScan(scanCallback)
                }, SCAN_PERIOD)
                scanning = true
                Timber.d("started scanning...")
                //bleRepository.deleteScans()
                btScanner.startScan(null, scanSettings, scanCallback)
            } else {
                scanning = false
                btScanner.stopScan(scanCallback)
                Timber.d("stopped scanning...")
            }
        }
    }*/

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


/*
private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
private var scanning = false
private val handler = Handler()

// Stops scanning after 10 seconds.
private val SCAN_PERIOD: Long = 10000

private fun scanLeDevice() {
    if (!scanning) { // Stops scanning after a pre-defined scan period.
        handler.postDelayed({
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }, SCAN_PERIOD)
        scanning = true
        bluetoothLeScanner.startScan(leScanCallback)
    } else {
        scanning = false
        bluetoothLeScanner.stopScan(leScanCallback)
    }
}*/
