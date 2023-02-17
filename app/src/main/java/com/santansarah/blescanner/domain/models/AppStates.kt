package com.santansarah.blescanner.domain.models

import com.santansarah.blescanner.data.local.entities.ScannedDevice

data class ScanState(
    //val isScanning: Boolean,
    val devices: List<ScannedDevice>,
    val selectedDevice: DeviceDetail?,
    val bleMessage: ConnectionState,
    val userMessage: String?,
    val scanFilterOption: ScanFilterOption?
)

enum class ConnectionState {
    CONNECTED,
    CONNECTING,
    DISCONNECTING,
    DISCONNECTED;

    fun toTitle() = this.name.lowercase().replaceFirstChar { it.uppercase() }
}

