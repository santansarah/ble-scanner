package com.santansarah.blescanner.presentation

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import com.santansarah.blescanner.data.local.BleDao
import com.santansarah.blescanner.data.local.BleDatabase
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.di.testAppModule
import com.santansarah.blescanner.di.testDatabaseModule
import com.santansarah.blescanner.di.testUsecasesModule
import com.santansarah.blescanner.domain.usecases.ParseScanResult
import com.santansarah.blescanner.util.AddKoinModules
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import java.io.IOException
import java.util.UUID

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class BleManagerTest : KoinTest {

    @JvmField
    @RegisterExtension
    val extension = AddKoinModules(
        testAppModule, testDatabaseModule, testUsecasesModule
    )

    private val bleDb: BleDatabase = get()
    private val bleRepository: BleRepository = get()
    private val parseScanResult: ParseScanResult = get()

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @AfterEach
    @Throws(IOException::class)
    fun closeDb() {
        bleDb.close()
    }

    @Test
    fun test() = runTest {

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
        val dbResult = bleRepository.getScannedDevices().first().find {
            it.address == "BE:FF:FA:00:11:22"
        }

        assertNotNull(dbResult)

        with(dbResult!!) {
            assertAll("Parser Database Verification",
                { assertEquals("ELK-BLEDOM", deviceName) },
                { assertEquals("Human Interface Device", services?.first()) },
                { assertEquals(-71, rssi) }
            )
        }

    }

}