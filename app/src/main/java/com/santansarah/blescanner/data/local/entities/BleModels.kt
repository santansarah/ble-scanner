package com.santansarah.blescanner.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class Company(
    @PrimaryKey val code: Int,
    val name: String
)

@Entity(tableName = "services")
data class Service(
    val identifier: String,
    val name: String,
    val source: String,
    @PrimaryKey val uuid: String
)

@Entity(tableName = "MicrosoftDevices")
data class MicrosoftDevice(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(tableName = "scanned")
data class ScannedDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val manufacturer: String,
    val services: List<String>
)





