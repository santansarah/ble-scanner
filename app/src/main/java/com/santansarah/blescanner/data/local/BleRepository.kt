package com.santansarah.blescanner.data.local

import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.Service
import kotlinx.coroutines.flow.Flow

class BleRepository(
    private val dao: BleDao
) {

    suspend fun getCompanyById(id: Int): Company? = dao.getCompanyById(id)

    suspend fun getServiceById(uuid: String): Service? = dao.getServiceByUuid(uuid)

    suspend fun getMicrosoftDeviceById(id: Int): MicrosoftDevice? = dao.getMicrosoftDevice(id)

    suspend fun insertDevice(device: ScannedDevice) = dao.insertDevice(device)

    fun deleteScans() = dao.deleteScans()

}
