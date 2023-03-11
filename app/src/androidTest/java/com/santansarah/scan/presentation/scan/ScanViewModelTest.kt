package com.santansarah.scan.presentation.scan

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import app.cash.turbine.test
import com.santansarah.scan.domain.interfaces.IAnalytics
import com.santansarah.scan.domain.models.ConnectionState
import com.santansarah.scan.domain.models.ScanFilterOption
import com.santansarah.scan.domain.usecases.ParseScanResult
import com.santansarah.scan.local.BleRepository
import com.santansarah.scan.presentation.BleGatt
import com.santansarah.scan.presentation.BleManager
import com.santansarah.scan.utils.decodeHex
import com.santansarah.sharedtest.deviceDetail
import com.santansarah.sharedtest.deviceList
import com.santansarah.sharedtest.existingDevice
import com.santansarah.sharedtest.newDevice
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ScanViewModelTest : KoinTest {

    private val analytics: IAnalytics = mockk(relaxed = true)
    private val btAdapter: BluetoothAdapter = get()
    private val btLeScanner: BluetoothLeScanner = get()
    private val bleGatt = mockk<BleGatt>(relaxed = true)

    private lateinit var bleManager: BleManager
    //private lateinit var bleGatt: BleGatt
    private lateinit var scanViewModel: ScanViewModel

    private val bleRepository = mockk<BleRepository>()
    private val parseScanResult = ParseScanResult(bleRepository)

    private val dispatcher = UnconfinedTestDispatcher()
    private val application: Application = get()

    @BeforeEach
    fun setup() {
        println("Before each...")
        clearAllMocks()

        every { btAdapter.bluetoothLeScanner } returns btLeScanner
        every { btAdapter.isEnabled } returns true

        bleManager = BleManager(
            bleRepository,
            TestScope(),
            parseScanResult
        )

        every { bleGatt.connectMessage } returns MutableStateFlow(ConnectionState.CONNECTED)
        every { bleGatt.deviceDetails } returns MutableStateFlow(deviceDetail.services)

        /*bleGatt = BleGatt(
            application, TestScope(),
            ParseService(bleRepository), ParseRead(),
            ParseNotification(), ParseDescriptor()
        )*/

        val flow = flow {
            emit(deviceList)
        }

        every { bleRepository.getScannedDevices(null) } returns flow
        every { bleRepository.getScannedDevices(ScanFilterOption.FAVORITES) } returns flow {
            emit(deviceList.filter { it.favorite })
        }

        scanViewModel = ScanViewModel(
            bleManager = bleManager, bleGatt = bleGatt,
            bleRepository = bleRepository, dispatcher = dispatcher, analytics = analytics
        )

    }

    @AfterEach
    fun tearDown() {
        //Dispatchers.resetMain()
    }

    @Test
    fun isEditing() {
    }

    @Test
    fun startScan() {
        scanViewModel.startScan()
        assertEquals(true, scanViewModel.isScanning.value)

        every { btAdapter.isEnabled } returns false
        scanViewModel.startScan()
        assertEquals(
            "You must enable Bluetooth to start scanning.",
            scanViewModel.scannerMessage.value
        )
    }

    @Test
    fun stopScan() {
        scanViewModel.stopScan()
        assertEquals(false, scanViewModel.isScanning.value)
    }

    @Test
    fun onFilter() = runTest {

        scanViewModel.scanState.test {
            awaitItem()

            val scanFilterOption = ScanFilterOption.FAVORITES
            scanViewModel.onFilter(scanFilterOption)

            val filterState = awaitItem()
            verify { bleRepository.getScannedDevices(scanFilterOption) }

            assertEquals(scanFilterOption, filterState.scanUI.scanFilterOption)

        }

    }

    @Test
    fun onFavorite() = runTest {

        coEvery { bleRepository.updateDevice(newDevice.copy(favorite = true)) } just runs

        scanViewModel.scanState.test {
            awaitItem()
            scanViewModel.onFavorite(newDevice)

            val deviceUpdatedMessage = awaitItem()
            assertNotNull(deviceUpdatedMessage.scanUI.userMessage)
        }

    }

    @Test
    fun onForget() = runTest {

        coEvery { bleRepository.updateDevice(newDevice.copy(forget = true)) } just runs

        scanViewModel.scanState.test {
            awaitItem()
            scanViewModel.onForget(newDevice)

            val deviceUpdatedMessage = awaitItem()
            assertNotNull(deviceUpdatedMessage.scanUI.userMessage)
        }

    }

    @Test
    fun onIsEditing() {
        scanViewModel.onIsEditing(true)
        assertEquals(true, scanViewModel.isEditing.value)
    }

    @Test
    fun onNameChange() = runTest {

        val onNameChange = existingDevice.copy(customName = "Unit Test")
        coEvery { bleRepository.updateDevice(onNameChange) } just runs
        coEvery { bleRepository.getDeviceByAddress(onNameChange.address) } returns onNameChange

        scanViewModel.scanState.test {
            awaitItem()
            scanViewModel.onConnect(onNameChange.address)
            awaitItem()

            scanViewModel.onNameChange("Unit Test")

            val deviceUpdatedMessage = awaitItem()
            //println(deviceUpdatedMessage.toString())
            assertNotNull(deviceUpdatedMessage.scanUI.userMessage)
        }
    }

    @Test
    fun onConnect() = runTest {

        scanViewModel.scanState.test {
            awaitItem()
            scanViewModel.onConnect(existingDevice.address)
            val selectedDevice = awaitItem()
            assertEquals(
                existingDevice.address, selectedDevice.scanUI
                    .selectedDevice?.scannedDevice?.address
            )

        }
    }

    @Test
    fun readCharacteristic() {
        val uuid = deviceDetail.services.first().characteristics.first().uuid
        scanViewModel.readCharacteristic(uuid)
        verify { bleGatt.readCharacteristic(uuid) }
    }

    @Test
    fun onWriteCharacteristic() {
        val uuid = deviceDetail.services.first().characteristics.first().uuid
        scanViewModel.onWriteCharacteristic(uuid, "FFFF00")
        verify { bleGatt.writeBytes(uuid, "FFFF00".decodeHex()) }
    }

    @Test
    fun readDescriptor() {
        val descriptor = deviceDetail.services.first().characteristics.first().descriptors.first()
        scanViewModel.readDescriptor(descriptor.charUuid, descriptor.uuid)
        verify { bleGatt.readDescriptor(descriptor.charUuid, descriptor.uuid) }
    }

    @Test
    fun writeDescriptor() {
        val descriptor = deviceDetail.services.first().characteristics.first().descriptors.first()
        scanViewModel.writeDescriptor(descriptor.charUuid, descriptor.uuid, "FFFF00")
        verify { bleGatt.writeDescriptor(descriptor.charUuid, descriptor.uuid, "FFFF00".decodeHex()) }
    }

    @Test
    fun onBackFromDevice() = runTest {

        scanViewModel.scanState.test {
            awaitItem()

            scanViewModel.onConnect(existingDevice.address)
            val selectedDevice = awaitItem()
            assertNotNull(selectedDevice.scanUI.selectedDevice)

            scanViewModel.onBackFromDevice()
            val clearSelectedDevice = awaitItem()
            assertNull(clearSelectedDevice.scanUI.selectedDevice)

            assertEquals(false, scanViewModel.isEditing.value)
            verify { bleManager.scan() }

            assertEquals(true, scanViewModel.isScanning.value)

        }

    }

    @Test
    fun onDisconnect() {
        scanViewModel.onDisconnect()
        verify { bleGatt.close() }
    }

    @Test
    fun userMessageShown() = runTest {
        scanViewModel.scanState.test {
            awaitItem()
            scanViewModel.showUserMessage("Unit Test.")
            val userMessage = awaitItem()
            assertNotNull(userMessage.scanUI.userMessage)
            scanViewModel.userMessageShown()
            val shown = awaitItem()
            assertNull(shown.scanUI.userMessage)
        }
    }

}