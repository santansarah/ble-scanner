package com.santansarah.scan.domain.models

import com.santansarah.scan.local.entities.ScannedDevice

data class ScanState(
    val scanUI: ScanUI,
    val bleConnectEvents: BleConnectEvents,
    val bleReadWriteCommands: BleReadWriteCommands,
    val deviceEvents: DeviceEvents
)

data class ScanUI(
    //val isScanning: Boolean,
    val devices: List<ScannedDevice>,
    val selectedDevice: DeviceDetail?,
    val bleMessage: ConnectionState,
    val userMessage: String?,
    val scanFilterOption: ScanFilterOption?,
)

data class BleConnectEvents(
    val onConnect: (String) -> Unit,
    val onDisconnect: () -> Unit,
)

data class BleReadWriteCommands(
    val onRead: (String) -> Unit,
    val onWrite: (String, String) -> Unit,
    val onReadDescriptor: (String, String) -> Unit,
    val onWriteDescriptor: (String, String, String) -> Unit
)

data class DeviceEvents(
    val onIsEditing: (Boolean) -> Unit,
    val onFavorite: (ScannedDevice) -> Unit,
    val onForget: (ScannedDevice) -> Unit,
    val onSave: (String) -> Unit,
    val onBack: () -> Unit
)

val emptyScanState = ScanState(
    ScanUI(
        devices = emptyList(),
        selectedDevice = null,
        bleMessage = ConnectionState.DISCONNECTED,
        userMessage = null,
        scanFilterOption = null,
    ),
    bleConnectEvents = BleConnectEvents({}, {}),
    bleReadWriteCommands = BleReadWriteCommands(
        {},
        { _: String, _: String -> },
        { _: String, _: String -> },
        { _: String, _: String, _: String -> },
    ),
    deviceEvents = DeviceEvents({}, {}, {}, {}, {})
)

data class ControlState(
    //val isScanning: Boolean,
    val device: ScannedDevice?,
    val services: List<DeviceService>,
    val bleMessage: ConnectionState,
    val userMessage: String?,
)

enum class ConnectionState {
    CONNECTED,
    CONNECTING,
    DISCONNECTING,
    DISCONNECTED,
    UNKNOWN;

    fun isActive() = (this == CONNECTING || this == CONNECTED)

    fun toTitle() = this.name.lowercase().replaceFirstChar { it.uppercase() }
}

