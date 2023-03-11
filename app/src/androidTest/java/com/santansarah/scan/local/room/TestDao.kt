package com.santansarah.scan.local.room

import androidx.room.Dao
import androidx.room.Insert
import com.santansarah.scan.local.entities.BleCharacteristic
import com.santansarah.scan.local.entities.Company
import com.santansarah.scan.local.entities.Descriptor
import com.santansarah.scan.local.entities.MicrosoftDevice
import com.santansarah.scan.local.entities.Service

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