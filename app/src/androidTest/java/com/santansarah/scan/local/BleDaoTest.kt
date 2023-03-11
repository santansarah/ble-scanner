package com.santansarah.scan.local

import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.local.room.TestBleDatabase
import com.santansarah.sharedtest.deviceList
import com.santansarah.sharedtest.newDevice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.test.KoinTest

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@OptIn(ExperimentalCoroutinesApi::class)
internal class BleDaoTest : KoinTest {

    private val testScope = getKoin().createScope<TestBleDatabase>()

    private val bleDb = testScope.get<TestBleDatabase>()
    private val bleDao = testScope.get<BleDao>()

    @AfterEach
    fun tearDown() {
        bleDb.close()
        testScope.close()
    }

    @Test
    @DisplayName("Insert device and read from flow")
    fun writeDeviceAndReadFromFlow() = runTest {
        bleDao.insertDevice(newDevice)
        val scannedDevices = bleDao.getScannedDevices().first()
        Assertions.assertEquals(newDevice.address, scannedDevices.first().address)
    }

    @Test
    @DisplayName("Verify the MAC address unique constraint")
    fun checkAddressConstraint() = runTest {
        bleDao.insertDevice(newDevice)
        bleDao.insertDevice(newDevice)
        val scannedDevices = bleDao.getScannedDevices().first()
        Assertions.assertEquals(1, scannedDevices.count {
            it.address == newDevice.address
        })
    }

    @Test
    @DisplayName("Verify all devices are deleted")
    fun deleteAllDevices() = runTest {
        deviceList.forEach {
            bleDao.insertDevice(it)
        }
        bleDao.deleteScans()
        val scannedDevices = bleDao.getScannedDevices().first()
        Assertions.assertEquals(emptyList<ScannedDevice>(), scannedDevices)
    }


}