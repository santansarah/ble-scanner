package com.santansarah.blescanner.data.local

import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.di.testDatabaseModule
import com.santansarah.blescanner.util.AddKoinModules
import com.santansarah.sharedtest.deviceList
import com.santansarah.sharedtest.newDevice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
internal class BleDaoTest : KoinTest {

    @JvmField
    @RegisterExtension
    val extension = AddKoinModules(
        testDatabaseModule
    )

    private val bleDb by inject<BleDatabase>()
    private val bleDao by inject<BleDao>()

    @AfterAll
    @Throws(IOException::class)
    fun closeDb() {
        bleDb.close()
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