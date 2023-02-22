package com.santansarah.blescanner.domain.models

import com.santansarah.blescanner.data.local.entities.ScannedDevice

data class ScanState(
    val scanUI: ScanUI,
    val bleConnectEvents: BleConnectEvents,
    val bleReadWriteCommands: BleReadWriteCommands
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

    fun toTitle() = this.name.lowercase().replaceFirstChar { it.uppercase() }
}

