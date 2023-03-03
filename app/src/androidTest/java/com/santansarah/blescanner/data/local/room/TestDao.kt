package com.santansarah.blescanner.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import com.santansarah.blescanner.data.local.entities.BleCharacteristic
import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.Descriptor
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.Service

@Dao
interface TestDao {

    @Insert
    fun insertServices(services: List<Service>)

    @Insert
    fun insertCompanies(companies: List<Company>)

    @Insert
    fun insertMicrosoftDevices(devices: List<MicrosoftDevice>)

    @Insert
    fun insertCharacteristics(characteristics: List<BleCharacteristic>)

    @Insert
    fun insertDescriptors(descriptor: List<Descriptor>)

}