package com.santansarah.scan.presentation

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import android.util.SparseArray
import com.santansarah.scan.domain.usecases.ParseScanResult
import com.santansarah.scan.local.BleRepository
import com.santansarah.scan.local.room.TestBleDatabase
import com.santansarah.scan.util.KoinTestPerMethod
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import java.util.UUID

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class BleManagerTest : KoinTest {

    private val testScope = getKoin().createScope<TestBleDatabase>()

    private val bleDb = testScope.get<TestBleDatabase>()
    private val bleRepository = testScope.get<BleRepository>()
    private val parseScanResult= ParseScanResult(bleRepository)

    @JvmField
    @RegisterExtension
    val extension = KoinTestPerMethod(bleDb)

    @AfterEach
    fun dispose() {
        testScope.close()
    }

    @BeforeEach
    fun setup() {
        println("Before each...")
        clearAllMocks()
    }

    @Test
    fun parseDeviceWithService() = runTest {

        val device = mockkClass(BluetoothDevice::class)
        every { device["getAddress"]() } returns "BE:FF:FA:00:11:22"
        every { device["getName"]() } returns "ELK-BLEDOM"

        val scanRecord = mockkClass(ScanRecord::class)
        every { scanRecord["getManufacturerSpecificData"]() } returns null

        every { scanRecord["getServiceUuids"]() } returns listOf(
            ParcelUuid(UUID.fromString("00001812-0000-1000-8000-00805f9b34fb"))
        )

        val scanResult = ScanResult(
            /* device = */ device,
            /* eventType = */ 27,
            /* primaryPhy = */ 1,
            /* secondaryPhy = */ 0,
            /* advertisingSid = */ 255,
            /* txPower = */ -2147483648,
            /* rssi = */ -71,
            /* periodicAdvertisingInterval = */ 0,
            /* scanRecord = */ scanRecord,
            /* timestampNanos = */ 1894004710612013
        )

        parseScanResult(scanResult)
        val dbResult = bleRepository.getScannedDevices(null).first().find {
            it.address == "BE:FF:FA:00:11:22"
        }
        println("db count: ${bleRepository.getScannedDevices(null).first().count()}")

        assertNotNull(dbResult)

        with(dbResult!!) {
            assertAll("Parser Database Verification",
                { assertEquals("ELK-BLEDOM", deviceName) },
                { assertEquals("Human Interface Device", services?.first()) },
                { assertEquals(-71, rssi) }
            )
        }

    }


    @Test
    fun parseMicrosoftDevice() = runTest {

        val device = mockkClass(BluetoothDevice::class)
        every { device["getAddress"]() } returns "BE:FF:FA:00:22:33"
        every { device["getName"]() } returns null

        val scanRecord = mockkClass(ScanRecord::class)
        val msByteArray = byteArrayOf(
            1, 9, 32, 34, 26, -20, 2, -83, -100, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        )
        var mfArray = SparseArray<ByteArray>().also {
            it.append(6, msByteArray)
        }
        every { scanRecord["getManufacturerSpecificData"]() } returns mfArray
        every { scanRecord.getManufacturerSpecificData(6) } returns msByteArray

        every { scanRecord["getServiceUuids"]() } returns null

        val scanResult = ScanResult(
            /* device = */ device,
            /* eventType = */ 27,
            /* primaryPhy = */ 1,
            /* secondaryPhy = */ 0,
            /* advertisingSid = */ 255,
            /* txPower = */ -2147483648,
            /* rssi = */ -30,
            /* periodicAdvertisingInterval = */ 0,
            /* scanRecord = */ scanRecord,
            /* timestampNanos = */ 1894004710612013
        )

        parseScanResult(scanResult)
        val dbResult = bleRepository.getScannedDevices(null).first().find {
            it.address == "BE:FF:FA:00:22:33"
        }

        assertNotNull(dbResult)

        with(dbResult!!) {
            assertAll("Parser Database Verification",
                { assertEquals("Microsoft", manufacturer) },
                { assertEquals("Windows 10 Desktop", extra?.first()) },
                { assertEquals(-30, rssi) }
            )
        }

    }

}