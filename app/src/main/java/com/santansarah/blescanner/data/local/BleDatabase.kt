package com.santansarah.blescanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.Service

@Database(
    entities = [Company::class, Service::class, MicrosoftDevice::class],
    version = 1, exportSchema = true
)
abstract class BleDatabase : RoomDatabase() {
    abstract fun bleDao(): BleDao
}