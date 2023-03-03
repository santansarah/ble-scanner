package com.santansarah.sharedtest.mocks

import android.os.ParcelUuid
import com.santansarah.blescanner.data.local.entities.BleCharacteristic
import com.santansarah.blescanner.data.local.entities.Company
import com.santansarah.blescanner.data.local.entities.Descriptor
import com.santansarah.blescanner.data.local.entities.MicrosoftDevice
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.Service
import com.santansarah.blescanner.domain.interfaces.IBleRepository
import com.santansarah.blescanner.domain.models.ScanFilterOption
import com.santansarah.sharedtest.deviceList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BleRepositoryMock: IBleRepository {
    override suspend fun getCompanyById(id: Int): Company? {
        TODO("Not yet implemented")
    }

    override suspend fun getServiceById(uuid: String): Service? {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacteristicById(uuid: String): BleCharacteristic? {
        TODO("Not yet implemented")
    }

    override suspend fun getMicrosoftDeviceById(id: Int): MicrosoftDevice? {
        TODO("Not yet implemented")
    }

    override suspend fun insertDevice(device: ScannedDevice): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getDeviceByAddress(address: String): ScannedDevice? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteScans() {
        TODO("Not yet implemented")
    }

    override fun getScannedDevices(scanFilter: ScanFilterOption?): Flow<List<ScannedDevice>> {

       return flow { deviceList }

    }

    override suspend fun getMsDevice(byteArray: ByteArray): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getServices(serviceIdRecord: List<ParcelUuid>): List<String>? {
        TODO("Not yet implemented")
    }

    override suspend fun getDescriptorById(uuid: String): Descriptor? {
        TODO("Not yet implemented")
    }

    override suspend fun updateDevice(scannedDevice: ScannedDevice) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotSeen() {
        TODO("Not yet implemented")
    }
}