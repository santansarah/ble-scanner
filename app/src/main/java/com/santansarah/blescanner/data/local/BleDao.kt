package com.santansarah.blescanner.data.local

import androidx.room.Dao
import androidx.room.Query
import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.Service

@Dao
interface BleDao {
    @Query("SELECT * FROM companies WHERE code = :companyId")
    fun getCompanyById(companyId: Int): Company?

    @Query("SELECT * FROM services where uuid = :uuid")
    fun getServiceByUuid(uuid: String): Service?

    @Query("SELECT * FROM MicrosoftDevices where id = :id")
    fun getMicrosoftDevice(id: Int): MicrosoftDevice?

}

