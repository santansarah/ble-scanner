package com.santansarah.sharedtest

import com.santansarah.scan.local.entities.BleCharacteristic
import com.santansarah.scan.local.entities.Descriptor

val characteristics = listOf(
    BleCharacteristic(
        name = "Device Name",
        identifier = "org.bluetooth.characteristic.gap.device_name",
        uuid = "2A00",
        source = "gss"
    ),
    BleCharacteristic(
        name = "Appearance",
        identifier = "org.bluetooth.characteristic.gap.appearance",
        uuid = "2A01",
        source = "gss"
    )
)

val descriptors = listOf(

    Descriptor(
        identifier = "org.bluetooth.descriptor.gatt.client_characteristic_configuration",
        name = "Client Characteristic Configuration",
        source = "gss",
        uuid = "2901"
    )

)