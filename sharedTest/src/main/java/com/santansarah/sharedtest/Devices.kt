package com.santansarah.sharedtest

import com.santansarah.blescanner.data.local.entities.ScannedDevice

val newDevice = ScannedDevice(
    deviceId = null,
    deviceName = "LED",
    address = "24:00:30:53:XX:97",
    rssi = -45,
    manufacturer = "Microsoft",
    services = listOf("Human Readable Device"),
    extra = listOf("Windows 10 Desktop"),
    lastSeen = 0L
)

val deviceList = listOf(
    ScannedDevice(
        deviceId = 0,
        deviceName = "ELK-BLEDOM",
        address = "BE:00:FA:00:XX:00",
        rssi = -77,
        manufacturer = null,
        services = listOf("[Human Interface Device"),
        extra = null,
        lastSeen = 1674510398719
    ),
    ScannedDevice(
        deviceId = 0,
        deviceName = "EASYWAY-BLE",
        address = "93:00:44:00:XX:AC",
        rssi = -81,
        manufacturer = "Ericsson Technology Licensing",
        services = null,
        extra = null,
        lastSeen = 1674510397416
    )
)