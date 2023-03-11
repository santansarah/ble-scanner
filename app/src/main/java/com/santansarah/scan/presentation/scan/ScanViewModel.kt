package com.santansarah.scan.presentation.scan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.local.entities.displayName
import com.santansarah.scan.domain.interfaces.IAnalytics
import com.santansarah.scan.domain.interfaces.IBleRepository
import com.santansarah.scan.domain.models.BleConnectEvents
import com.santansarah.scan.domain.models.BleReadWriteCommands
import com.santansarah.scan.domain.models.DeviceDetail
import com.santansarah.scan.domain.models.DeviceEvents
import com.santansarah.scan.domain.models.ScanFilterOption
import com.santansarah.scan.domain.models.ScanState
import com.santansarah.scan.domain.models.ScanUI
import com.santansarah.scan.domain.models.emptyScanState
import com.santansarah.scan.presentation.BleGatt
import com.santansarah.scan.presentation.BleManager
import com.santansarah.scan.utils.decodeHex
import com.santansarah.scan.utils.logging.AnalyticsEventType
import com.santansarah.scan.utils.logging.CharacteristicEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalCoroutinesApi::class)
class ScanViewModel(
    private val bleManager: BleManager,
    private val bleGatt: BleGatt,
    private val bleRepository: IBleRepository,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: IAnalytics
) : ViewModel() {

    val isScanning by mutableStateOf(bleManager.isScanning)
    val scannerMessage = bleManager.userMessage

    private val _scanFilterOption = MutableStateFlow<ScanFilterOption?>(null)
    private val _devices = _scanFilterOption.flatMapLatest { scanFilterOption ->
        bleRepository.getScannedDevices(scanFilterOption).map {
            Pair(scanFilterOption, it)
        }
    }
    private val _selectedDevice = MutableStateFlow<ScannedDevice?>(null)
    private val _bleMessage = bleGatt.connectMessage
    private val _userMessage = MutableStateFlow<String?>(null)
    private val _deviceDetails =
        combine(_selectedDevice, bleGatt.deviceDetails) { selectedDevice, deviceDetails ->
            selectedDevice?.let {
                DeviceDetail(it, deviceDetails)
            }
        }
    val isEditing = MutableStateFlow(false)

    val scanState = combine(
        _devices, _bleMessage, _userMessage, _deviceDetails
    ) { devices, bleMessage, userMessage, deviceDetails ->

        if (deviceDetails != null)
            bleManager.scanEnabled = false

        ScanState(
            ScanUI(
                devices.second,
                deviceDetails,
                bleMessage,
                userMessage,
                devices.first,
            ),
            bleConnectEvents(),
            bleReadWriteCommands(),
            deviceEvents()
        )
    }.flowOn(dispatcher)
        //.onStart { emit("Loading...") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyScanState
        )

    private fun deviceEvents() = DeviceEvents(
        onIsEditing = {
            onIsEditing(it)
        },
        onFavorite = {
            onFavorite(it)
        },
        onForget = {
            onForget(it)
        },
        onSave = {
            onNameChange(it)
        },
        onBack = {
            onBackFromDevice()
        }
    )

    private fun bleReadWriteCommands() = BleReadWriteCommands(
        onRead = { readCharacteristic(it) },
        onWrite = { uuid, bytes ->
            onWriteCharacteristic(uuid, bytes)
        },
        onReadDescriptor = { charUuid, uuid ->
            readDescriptor(charUuid, uuid)
        },
        onWriteDescriptor = { charUuid, descUuid, hexString ->
            writeDescriptor(charUuid, descUuid, hexString)
        }
    )

    private fun bleConnectEvents() = BleConnectEvents(
        onConnect = {
            onConnect(it)
        },
        onDisconnect = { onDisconnect() }
    )

    fun startScan() {
        bleManager.scanEnabled = true
        bleManager.scan()
    }

    fun stopScan() {
        bleManager.scanEnabled = false
        bleManager.stopScan()
    }

    fun onFilter(scanFilterOption: ScanFilterOption?) {
        Timber.d(scanFilterOption?.name ?: "was empty")
        _scanFilterOption.value = scanFilterOption
    }

    fun onFavorite(scannedDevice: ScannedDevice) {
        viewModelScope.launch(dispatcher) {

            val isFavorite = !scannedDevice.favorite
            val favoriteAction = if (isFavorite) "added to" else "removed from"

            bleRepository.updateDevice(scannedDevice.copy(favorite = isFavorite))
            showUserMessage("${scannedDevice.displayName()} $favoriteAction Favorites.")
        }
    }

    fun onForget(scannedDevice: ScannedDevice) {
        viewModelScope.launch(dispatcher) {
            bleRepository.updateDevice(scannedDevice.copy(forget = true))
            _selectedDevice.value = null
            showUserMessage("${scannedDevice.displayName()} forgotten.")
        }
    }

    fun onIsEditing(value: Boolean) {
        isEditing.value = value
    }

    fun onNameChange(newName: String) {
        val currentDevice = _selectedDevice.value
        currentDevice?.let {
            viewModelScope.launch(dispatcher) {
                bleRepository.updateDevice(it.copy(customName = newName))
                _selectedDevice.value = bleRepository.getDeviceByAddress(it.address)
                showUserMessage("$newName updated.")
            }
        }
    }

    fun onConnect(address: String) {
        Timber.d("calling connect...")
        val scannedDevice = scanState.value.scanUI.devices.find {
            it.address == address
        }

        scannedDevice?.let {
            _selectedDevice.value = scannedDevice
            stopScan()
            bleGatt.connect(address)
        }
    }

    fun readCharacteristic(uuid: String) {
        bleGatt.readCharacteristic(uuid)
        analytics.logCharacteristicEvent(
            CharacteristicEvent(
            eventName = AnalyticsEventType.READ_CHARACTERISTIC.name,
            uuid = uuid
        )
        )
        //showUserMessage("Request sent.")
    }

    fun readDescriptor(charUuid: String, descUuid: String) {
        bleGatt.readDescriptor(charUuid, descUuid)
        //showUserMessage("Request sent.")
    }

    fun writeDescriptor(charUuid: String, descUuid: String, hexString: String) {
        try {
            if (hexString.isNotEmpty()) {
                bleGatt.writeDescriptor(charUuid, descUuid, hexString.decodeHex())
                //showUserMessage("Data sent.")
            } else
                showUserMessage("Hex can't be null.")
        } catch (badHex: Exception) {
            showUserMessage("Invalid Hex String. Must be an even count.")
        }
    }

    fun onBackFromDevice() {
        bleGatt.close()
        _selectedDevice.value = null
        isEditing.value = false
        bleManager.scanEnabled = true
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
                analytics.logCharacteristicEvent(
                    CharacteristicEvent(
                    eventName = AnalyticsEventType.WRITE_CHARACTERISTIC.name,
                    uuid = uuid
                )
                )
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
        Timber.tag("debug").d("user message set to null.")
        _userMessage.value = null
    }

    fun scannerMessageShown() {
        bleManager.userMessageShown()
    }


}