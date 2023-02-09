package com.santansarah.blescanner.presentation.scan

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceDetail
import com.santansarah.blescanner.presentation.BleGatt
import com.santansarah.blescanner.presentation.BleManager
import com.santansarah.blescanner.utils.decodeHex
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.lang.Exception

data class ScanState(
    val devices: List<ScannedDevice>,
    val selectedDevice: DeviceDetail?,
    val bleMessage: ConnectionState,
    val userMessage: String?
)


class ScanViewModel(
    private val bleManager: BleManager,
    private val bleGatt: BleGatt,
    bleRepository: BleRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _devices = bleRepository.getScannedDevices()
    private val _selectedDevice = MutableStateFlow<DeviceDetail?>(null)
    private val _bleMessage = bleGatt.connectMessage
    private val _userMessage = MutableStateFlow<String?>(null)

    private val _deviceDetails = bleGatt.deviceDetails

    val scanState = combine(
        _devices, _selectedDevice,
        _bleMessage, _userMessage, _deviceDetails
    ) { devices, selectedDevice, bleMessage, userMessage, deviceDetails ->

        Timber.d(deviceDetails.toString())

        val currentDevice = selectedDevice?.let {
            DeviceDetail(
                it.scannedDevice,
                deviceDetails
            )
        }

        ScanState(
            devices,
            currentDevice,
            bleMessage,
            userMessage
        )
    }.flowOn(dispatcher)
        //.onStart { emit("Loading...") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ScanState(emptyList(), null, ConnectionState.DISCONNECTED, null)
        )


    fun startScan() {
        bleManager.scan()
    }

    fun stopScan() {
        bleManager.stopScan()
    }

    fun onConnect(address: String) {
        Timber.d("calling connect...")
        val scannedDevice = scanState.value.devices.find {
            it.address == address
        }

        scannedDevice?.let {
            _selectedDevice.value = DeviceDetail(
                scannedDevice,
                emptyList()
            )
            stopScan()
            bleGatt.connect(address)
        }
    }

    fun readCharacteristic(uuid: String) {
        bleGatt.readCharacteristic(uuid)
    }

    fun onBackFromDevice() {
        bleGatt.close()
        _selectedDevice.value = null
        startScan()
    }

    fun onDisconnect() {
        Timber.d("calling disconnect...")
        bleGatt.close()
    }

    fun onWriteCharacteristic(uuid: String, bytes: String) {
        try {
            if (bytes.isNotEmpty()) {
                bleGatt.writeBytes(uuid, bytes.decodeHex())
            } else
                showUserMessage("Hex can't be null.")
        } catch (badHex: Exception) {
            showUserMessage("Invalid Hex String. Must be an even count.")
        }
    }

    fun showUserMessage(message: String) {
        Timber.d("show message...")
        _userMessage.value = message
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _userMessage.value = null
    }


}