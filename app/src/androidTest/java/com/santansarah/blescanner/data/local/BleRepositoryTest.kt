package com.santansarah.blescanner.data.local

import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.displayName
import com.santansarah.blescanner.data.local.room.TestBleDatabase
import com.santansarah.blescanner.domain.models.ScanFilterOption
import com.santansarah.blescanner.util.KoinTestPerMethod
import com.santansarah.sharedtest.deviceList
import com.santansarah.sharedtest.newDevice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@OptIn(ExperimentalCoroutinesApi::class)
internal class BleRepositoryTest : KoinTest {

    private val testScope = getKoin().createScope<TestBleDatabase>()

    private val bleDb = testScope.get<TestBleDatabase>()
    private val bleRepository = testScope.get<BleRepository>()

    @JvmField
    @RegisterExtension
    val extension = KoinTestPerMethod(bleDb)

    @AfterEach
    fun tearDown() {
        bleDb.close()
        testScope.close()
    }

    @Test
    @DisplayName("Get BLE Company by id")
    fun getCompanyById() = runTest {
        val microsoft = bleRepository.getCompanyById(6)
        assertEquals("Microsoft", microsoft?.name)
    }

    @Test
    @DisplayName("Get BLE Service by UUID")
    fun getServiceById() = runTest {
        val human = bleRepository.getServiceById("1812")
        assertEquals("Human Interface Device", human?.name)
    }

    @Test
    @DisplayName("Get BLE Characteristic by UUID")
    fun getCharacteristicById() = runTest {
        val deviceName = bleRepository.getCharacteristicById("2A00")
        assertEquals("Device Name", deviceName?.name)
    }

    @Test
    @DisplayName("Insert device and read from flow")
    fun insertDevice() = runTest {
        bleRepository.insertDevice(newDevice)
        val scannedDevices = bleRepository.getScannedDevices(null).first()
        assertEquals(newDevice.address, scannedDevices.first().address)
    }

    @Test
    @DisplayName("Insert and preserve or update existing")
    fun insertWithExisting() = runTest {
        val existingDevice = ScannedDevice(
            deviceId = null,
            deviceName = "Custom Name",
            address = "24:00:30:53:XX:97",
            rssi = -45,
            manufacturer = "Microsoft",
            services = listOf("Human Readable Device"),
            extra = listOf("Windows 10 Desktop"),
            lastSeen = 1674510398719,
            customName = null,
            baseRssi = -45,
            favorite = false,
            forget = false
        )
        bleRepository.insertDevice(existingDevice)

        var verify = bleRepository.getDeviceByAddress("24:00:30:53:XX:97")
        assertNotNull(verify)

        val updatedDevice = ScannedDevice(
            deviceId = verify?.deviceId,
            deviceName = "Real Name",
            address = "24:00:30:53:XX:97",
            rssi = -66,
            manufacturer = "Microsoft",
            services = listOf("Human Readable Device"),
            extra = listOf("Windows 10 Desktop"),
            lastSeen = 1674510398733,
            customName = null,
            baseRssi = -45,
            favorite = false,
            forget = false
        )
        bleRepository.insertDevice(updatedDevice)
        verify = bleRepository.getDeviceByAddress("24:00:30:53:XX:97")
        assertNotNull(verify)

        with(verify!!) {
            assertAll("Verify Insert/Update",
                { assertEquals("Custom Name", displayName()) },
                { assertEquals(-66, baseRssi) },
                { assertEquals(1674510398733, lastSeen) },
                { assertEquals(-66, rssi) }
            )
        }
    }

    @Test
    @DisplayName("Verify all devices are deleted")
    fun deleteScans() = runTest {
        deviceList.forEach {
            bleRepository.insertDevice(it)
        }
        bleRepository.deleteScans()
        val scannedDevices = bleRepository.getScannedDevices(null).first()
        assertEquals(emptyList<ScannedDevice>(), scannedDevices)
    }

    @Test
    fun testScanFilters() = runTest {
        deviceList.forEach {
            bleRepository.insertDevice(it)
        }

        var devices = bleRepository.getScannedDevices(ScanFilterOption.NAME)
        assertEquals("EASYWAY-BLE", devices.first().first().displayName())

        devices = bleRepository.getScannedDevices(ScanFilterOption.RSSI)
        assertEquals("GOOD-RSSI", devices.first().first().displayName())
    }

}