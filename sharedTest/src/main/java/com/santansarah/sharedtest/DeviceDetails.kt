package com.santansarah.sharedtest

import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDetail
import com.santansarah.blescanner.domain.models.DeviceService

val deviceNameCharacteristic = DeviceCharacteristics(
    uuid = "00002a00-0000-1000-8000-00805f9b34fb",
    name = "Device Name",
    descriptor = null,
    permissions = 0,
    properties = 2,
    writeTypes = 2,
    descriptors = emptyList(),
    canRead = true,
    canWrite = false,
    readBytes = null
)

val deviceAppearanceCharacteristic = DeviceCharacteristics(
    uuid = "00002a00-0000-1000-8000-00805f9b34fb",
    name = "Appearance",
    descriptor = null,
    permissions = 0,
    properties = 2,
    writeTypes = 2,
    descriptors = emptyList(),
    canRead = true,
    canWrite = false,
    readBytes = null
)

val genericService = DeviceService(
    uuid = "1800",
    name = "Generic Access",
    characteristics = listOf(
        deviceNameCharacteristic,
        deviceAppearanceCharacteristic
    )
)

val mfrCharacteristic = DeviceCharacteristics(
    uuid = "0000ae01-0000-1000-8000-00805f9b34fb",
    name = "Mfr Characteristic",
    descriptor = null,
    permissions = 0,
    properties = 8,
    writeTypes = 2,
    descriptors = emptyList(),
    canRead = false,
    canWrite = true,
    readBytes = null
)

val mfrService = DeviceService(
    uuid = "0000ae00-0000-1000-8000-00805f9b34fb",
    name = "Mfr Service",
    characteristics = listOf(
        mfrCharacteristic
    )
)

val deviceDetail = DeviceDetail(
    scannedDevice =
    ScannedDevice(
        deviceId = 41,
        deviceName = "EASYWAY-BLE",
        address = "93:58:00:27:XX:00",
        rssi = -93,
        manufacturer = "Ericsson Technology Licensing",
        services = listOf("Heart Rate"),
        extra = null,
        lastSeen = 1675293173796
    ),
    services = listOf(
        genericService,
        mfrService
    )
)

