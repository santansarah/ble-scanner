package com.santansarah.sharedtest

import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.Service

val companies = listOf(

    Company(0, "Ericsson Technology Licensing"),
    Company(6, "Microsoft"),

    )

val services = listOf(
    Service(
        name = "Human Interface Device",
        identifier = "org.bluetooth.service.human_interface_device",
        uuid = "1812", source = "gss"
    ),
    Service(
        name = "Nordic LED and Button Service",
        identifier = "com.nordicsemi.service.led_and_button",
        uuid = "00001523-1212-EFDE-1523-785FEABCD123",
        source = "nordic"
    ),

    )

val msDevices = listOf(
    MicrosoftDevice(9, "Windows 10 Desktop")
)