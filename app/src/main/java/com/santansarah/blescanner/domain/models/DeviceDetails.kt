package com.santansarah.blescanner.domain.models

import com.santansarah.blescanner.data.local.entities.ScannedDevice

data class DeviceDetail(
    val scannedDevice: ScannedDevice,
    val services: List<DeviceService>
)

data class DeviceService(
    val uuid: String,
    val name: String,
    val characteristics: List<DeviceCharacteristics>
)


