package com.santansarah.scan.presentation

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import com.santansarah.scan.domain.interfaces.IBleRepository
import com.santansarah.scan.domain.usecases.ParseScanResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber

class BleManager(
    private val bleRepository: IBleRepository,
    private val scope: CoroutineScope,
    private val parseScanResult: ParseScanResult
) : KoinComponent {

    private val btAdapter: BluetoothAdapter = get()
    private val btScanner = btAdapter.bluetoothLeScanner

    val userMessage = MutableStateFlow<String?>(null)
    val isScanning = MutableStateFlow(true)

    var scanEnabled = false

    private var lastCleanupTimestamp: Long? = null
    private val CLEANUP_DURATION = 60000L

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val scanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            Timber.d("scanEnabled: $scanEnabled")

            scope.launch {
                parseScanResult(result)
                if (isScanning.value)
                    launch { deleteNotSeen() }
            }

        }

    }

    suspend fun deleteNotSeen() {
        lastCleanupTimestamp?.let {
            if (System.currentTimeMillis() - it > CLEANUP_DURATION) {
                bleRepository.deleteNotSeen()
                Timber.d("deleted not seen")
                lastCleanupTimestamp = System.currentTimeMillis()
            }
        }
    }

    init {
        if (!btAdapter.isEnabled)
            isScanning.value = false

        /*scope.launch {
            bleRepository.deleteScans()
            //scan()
        }*/
    }

    @SuppressLint("MissingPermission")
    fun scan() {
        if (scanEnabled) {
            if (btAdapter.isEnabled) {
                isScanning.value = true
                btScanner?.startScan(null, scanSettings, scanCallback)
                lastCleanupTimestamp = System.currentTimeMillis()
                Timber.d("started scan")
            } else {
                userMessage.value = "You must enable Bluetooth to start scanning."
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        try {
            if (btAdapter.isEnabled)
                btScanner.stopScan(scanCallback)
        } catch (e: Exception) {
            Timber.d(e.message)
        }
        finally {
            isScanning.value = false
        }
    }

    fun userMessageShown() {
        userMessage.value = null
    }

}