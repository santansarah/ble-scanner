package com.santansarah.blescanner.presentation.scan

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import app.cash.turbine.test
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.domain.models.ScanFilterOption
import com.santansarah.blescanner.domain.usecases.ParseDescriptor
import com.santansarah.blescanner.domain.usecases.ParseNotification
import com.santansarah.blescanner.domain.usecases.ParseRead
import com.santansarah.blescanner.domain.usecases.ParseScanResult
import com.santansarah.blescanner.domain.usecases.ParseService
import com.santansarah.blescanner.presentation.BleGatt
import com.santansarah.blescanner.presentation.BleManager
import com.santansarah.sharedtest.deviceList
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ScanViewModelTest : KoinTest {

    private val btAdapter: BluetoothAdapter = get()
    private val btLeScanner: BluetoothLeScanner = get()

    private lateinit var bleManager: BleManager
    private lateinit var bleGatt: BleGatt
    private lateinit var scanViewModel: ScanViewModel

    private val bleRepository = mockk<BleRepository>()
    private val parseScanResult = ParseScanResult(bleRepository)

    private val ioDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(ioDispatcher)
    private val application: Application = get()

    @BeforeEach
    fun setup() {
        println("Before each...")
        clearAllMocks()

        every { btAdapter.bluetoothLeScanner } returns btLeScanner
        every { btAdapter.isEnabled } returns true

        bleManager = BleManager(
            bleRepository,
            testScope,
            parseScanResult
        )

        bleGatt = BleGatt(
            application, testScope,
            ParseService(bleRepository), ParseRead(),
            ParseNotification(), ParseDescriptor()
        )

        val flow = flow {
            emit(deviceList)
        }

        every { bleRepository.getScannedDevices(null) } returns flow

        scanViewModel = ScanViewModel(
            bleManager = bleManager, bleGatt = bleGatt,
            bleRepository = bleRepository, dispatcher = ioDispatcher
        )

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
    fun getDeviceListState() {


    }

    @Test
    fun getSelectedDeviceState() {


    }

    @Test
    fun flowTest() = runTest {

        val job = launch(UnconfinedTestDispatcher()) {
            scanViewModel.scanState.collect {}
        }

        println(scanViewModel.scanState.value)


        job.cancel()

    }

    @Test
    fun onFilter() = runTest {

        scanViewModel.scanState.test {

            val initialState = awaitEvent()
            scanViewModel.onFilter(null)
            val devices = awaitItem()

            val scanFilterOption = ScanFilterOption.FAVORITES
            scanViewModel.onFilter(scanFilterOption)
            assertEquals(scanFilterOption, scanViewModel.scanState.value.scanUI.scanFilterOption)

            verify { bleRepository.getScannedDevices(scanFilterOption) }

        }

    }

    @Test
    fun onFavorite() {
    }

    @Test
    fun onForget() {
    }

    @Test
    fun onIsEditing() {
    }

    @Test
    fun onNameChange() {
    }

    @Test
    fun onConnect() {
    }

    @Test
    fun readCharacteristic() {
    }

    @Test
    fun readDescriptor() {
    }

    @Test
    fun writeDescriptor() {
    }

    @Test
    fun onBackFromDevice() {
    }

    @Test
    fun onDisconnect() {
    }

    @Test
    fun onWriteCharacteristic() {
    }

    @Test
    fun showUserMessage() {
    }

    @Test
    fun userMessageShown() {
    }

    @Test
    fun scannerMessageShown() {
    }
}