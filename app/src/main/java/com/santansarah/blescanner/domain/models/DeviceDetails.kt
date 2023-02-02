package com.santansarah.blescanner.domain.models

import androidx.compose.ui.text.capitalize
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import timber.log.Timber

enum class ConnectionState {
    CONNECTED,
    CONNECTING,
    DISCONNECTING,
    DISCONNECTED;

    fun toTitle() = this.name.lowercase().replaceFirstChar { it.uppercase() }
}

data class DeviceDetail(
    val scannedDevice: ScannedDevice,
    val services: List<DeviceService>
)

data class DeviceService(
    val uuid: String,
    val name: String,
    val characteristics: List<DeviceCharacteristics>
)

data class DeviceCharacteristics(
    val uuid: String,
    val name: String,
    val descriptor: String?,
    val permissions: Int,
    val properties: Int,
    val writeTypes: Int,
    val descriptors: List<DeviceDescriptor>,
    val canRead: Boolean,
    val canWrite: Boolean,
    val readValue: String?
) {
    fun updateReadValue(fromDevice: String): DeviceCharacteristics {
        Timber.d("fromDevice: $fromDevice")
        return copy(readValue = fromDevice)
    }
}

data class DeviceDescriptor(
    val uuid: String,
    val permissions: Int
)


