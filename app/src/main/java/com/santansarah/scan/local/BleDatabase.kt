package com.santansarah.scan.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.santansarah.scan.local.entities.BleCharacteristic
import com.santansarah.scan.local.entities.Company
import com.santansarah.scan.local.entities.Descriptor
import com.santansarah.scan.local.entities.MicrosoftDevice
import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.local.entities.Service
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Database(
    entities = [Company::class, Service::class, BleCharacteristic::class,
        MicrosoftDevice::class, ScannedDevice::class, Descriptor::class],
    version = 1, exportSchema = true
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