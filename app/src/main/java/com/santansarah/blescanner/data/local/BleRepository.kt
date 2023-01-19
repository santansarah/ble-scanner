package com.santansarah.blescanner.data.local

import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.Service

class BleRepository(
    private val dao: BleDao
) {

    suspend fun getCompanyById(id: Int): Company? = dao.getCompanyById(id)

    suspend fun getServiceById(uuid: String): Service? = dao.getServiceByUuid(uuid)

    suspend fun getMicrosoftDeviceById(id: Int): MicrosoftDevice? = dao.getMicrosoftDevice(id)

    suspend fun insertDevice(device: ScannedDevice): Long {
        val existingDevice = dao.getDeviceByAddress(device.address)

        val deviceToUpsert = ScannedDevice(
            deviceId = existingDevice?.deviceId,
            deviceName = existingDevice?.deviceName ?: device.deviceName,
            address = device.address,
            rssi = device.rssi,
            manufacturer = existingDevice?.manufacturer ?: device.manufacturer,
            services = device.services,
            extra = device.extra
        )

        return dao.insertDevice(deviceToUpsert)
    }

    suspend fun getDeviceByAddress(address: String) = dao.getDeviceByAddress(address)

    suspend fun deleteScans() = dao.deleteScans()

    fun getScannedDevices() = dao.getScannedDevices()

}
