package com.santansarah.blescanner.data.local.entities

import androidx.room.Entity
import androidx.room.Index
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

@Entity(tableName = "characteristics")
data class BleCharacteristic(
    @PrimaryKey val identifier: String,
    val name: String,
    val source: String,
    val uuid: String
)


@Entity(tableName = "MicrosoftDevices")
data class MicrosoftDevice(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(
    tableName = "scanned",
    indices = [Index(value = ["address"], unique = true)]
)
data class ScannedDevice(
    @PrimaryKey(autoGenerate = true) val deviceId: Long? = null,
    val deviceName: String?,
    val address: String,
    val rssi: Int,
    val manufacturer: String?,
    val services: List<String>? = null,
    val extra: List<String>? = null,
    val lastSeen: Long,
    val customName: String?,
    val baseRssi: Int,
    val favorite: Boolean
)

@Entity(tableName = "descriptors")
data class Descriptor(
    val identifier: String,
    val name: String,
    val source: String,
    @PrimaryKey val uuid: String
)
