package com.santansarah.scan.domain.models

import com.santansarah.scan.local.entities.ScannedDevice

data class DeviceDetail(
    val scannedDevice: ScannedDevice,
    val services: List<DeviceService>
)

data class DeviceService(
    val uuid: String,
    val name: String,
    val characteristics: List<DeviceCharacteristics>
)


