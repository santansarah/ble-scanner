package com.santansarah.scan.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.santansarah.scan.local.BleDao
import com.santansarah.scan.local.Converters
import com.santansarah.scan.local.entities.BleCharacteristic
import com.santansarah.scan.local.entities.Company
import com.santansarah.scan.local.entities.Descriptor
import com.santansarah.scan.local.entities.MicrosoftDevice
import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.local.entities.Service

@Database(
    entities = [Company::class, Service::class, BleCharacteristic::class, MicrosoftDevice::class,
        ScannedDevice::class, Descriptor::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TestBleDatabase : RoomDatabase() {
    abstract fun bleDao(): BleDao
    abstract fun testDao(): TestDao
}
