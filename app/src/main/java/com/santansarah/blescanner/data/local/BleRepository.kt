package com.santansarah.blescanner.data.local

import android.os.ParcelUuid
import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.Service
import com.santansarah.blescanner.utils.toGss
import com.santansarah.blescanner.utils.toHex

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
            extra = device.extra,
            lastSeen = device.lastSeen
        )

        return dao.insertDevice(deviceToUpsert)
    }

    suspend fun getDeviceByAddress(address: String) = dao.getDeviceByAddress(address)

    suspend fun deleteScans() = dao.deleteScans()

    fun getScannedDevices() = dao.getScannedDevices()

    suspend fun getMsDevice(
        byteArray: ByteArray
    ): String? {
        val msDeviceType = byteArray[1].toHex().toInt()
        return getMicrosoftDeviceById(msDeviceType)?.name
    }

    suspend fun getServices(
        serviceIdRecord: List<ParcelUuid>
    ): List<String>? {
        var serviceNames: MutableList<String>? = null

        serviceIdRecord.forEach { serviceId ->
            val formattedId = serviceId.uuid.toGss()
            getServiceById(formattedId)?.name?.let { serviceName ->
                if (serviceNames == null)
                    serviceNames = mutableListOf()
                serviceNames?.add(serviceName)
            }
        }

        return serviceNames?.toList()

    }


}
