package com.santansarah.blescanner.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.santansarah.blescanner.data.local.BleDao
import com.santansarah.blescanner.data.local.Converters
import com.santansarah.blescanner.data.local.entities.BleCharacteristic
import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.Descriptor
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.Service

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
