package com.santansarah.blescanner.domain

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.domain.models.ScannedDevice
import com.santansarah.blescanner.utils.toGss
import com.santansarah.blescanner.utils.toHex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes


class BLEManager(
    private val app: Application,
    private val bleRepository: BleRepository,
    private val scope: CoroutineScope
) {

    private val btManager = app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val btAdapter: BluetoothAdapter = btManager.adapter
    private val btScanner = btAdapter.bluetoothLeScanner

    private lateinit var btEnableResultLauncher: ActivityResultLauncher<Intent>

    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())

    private val SCAN_PERIOD: Long = 2.minutes.inWholeMilliseconds

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val scanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {

            scope.launch {
                Timber.d("device: $result")

                var device = ScannedDevice(
                    name = result.device.name ?: "Unknown",
                    address = result.device.address,
                    rssi = result.rssi,
                    manufacturer = null,
                    services = emptyList(),
                    extra = emptyList()
                )

                val mfgData = result.scanRecord?.manufacturerSpecificData
                mfgData?.let {
                    var mfId: Int = 0
                    for (i in 0 until it.size()) {
                        mfId = it.keyAt(i)
                    }
                    device = device.copy(manufacturer = bleRepository
                        .getCompanyById(mfId)?.name)

                    if (mfId == 6) {
                        val bytes = result.scanRecord?.getManufacturerSpecificData(mfId)
                        bytes?.let {msData ->
                            val msDeviceType = msData[1].toHex().toInt()
                            device = device.copy(extra = listOf(
                                bleRepository.getMicrosoftDeviceById(msDeviceType)?.name
                            ))
                        }
                    }

                }

                val serviceIdsRecord = result.scanRecord?.serviceUuids
                val serviceNames = mutableListOf<String?>()
                serviceIdsRecord?.let {
                    it.forEach { serviceId ->
                        val formattedId = serviceId.uuid.toGss()
                        serviceNames.add(bleRepository.getServiceById(formattedId)?.name)
                    }

                    device = device.copy(services = serviceNames)
                }

                Timber.d(device.toString())

            }

        }

    }

    init {
        setUpLauncher()
        checkEnabled()
        scanLeDevice()
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
        if (!btAdapter.isEnabled) {
            val btEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            btEnableResultLauncher.launch(btEnableIntent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                Timber.d("stopped scanning...")
                btScanner.stopScan(scanCallback)
            }, SCAN_PERIOD)
            scanning = true
            Timber.d("started scanning...")
            btScanner.startScan(null, scanSettings, scanCallback)
        } else {
            scanning = false
            btScanner.stopScan(scanCallback)
            Timber.d("stopped scanning...")
        }
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
