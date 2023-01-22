package com.santansarah.blescanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.Service
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Database(
    entities = [Company::class, Service::class,
        MicrosoftDevice::class, ScannedDevice::class],
    version = 7, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BleDatabase : RoomDatabase() {
    abstract fun bleDao(): BleDao
}

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?) = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(value: String) =
        if (value.startsWith("["))
            Json.decodeFromString<List<String>>(value)
        else null

}