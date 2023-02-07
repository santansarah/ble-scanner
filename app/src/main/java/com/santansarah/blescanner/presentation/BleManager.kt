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
import com.santansarah.blescanner.domain.usecases.ParseScanResult
import com.santansarah.blescanner.utils.toGss
import com.santansarah.blescanner.utils.toHex
import com.santansarah.blescanner.utils.toMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber

class BleManager(
    private val bleRepository: BleRepository,
    private val scope: CoroutineScope,
    private val parseScanResult: ParseScanResult
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
            super.onScanResult(callbackType, result)

            scope.launch {
                parseScanResult(result)
            }

        }

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