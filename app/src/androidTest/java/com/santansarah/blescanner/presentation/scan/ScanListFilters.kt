package com.santansarah.blescanner.presentation.scan

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.santansarah.blescanner.di.endtoend.endToEndModule
import com.santansarah.blescanner.domain.interfaces.IBleRepository
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.ScanFilterOption
import com.santansarah.blescanner.presentation.BleGatt
import com.santansarah.blescanner.presentation.BleManager
import com.santansarah.blescanner.presentation.MainActivity
import com.santansarah.blescanner.util.saveScreenshot
import com.santansarah.sharedtest.deviceDetail
import com.santansarah.sharedtest.deviceList
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import io.mockk.clearAllMocks
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class ScanListFilters : KoinTest {

    @RegisterExtension
    @JvmField
    val extension = createAndroidComposeExtension<MainActivity>()

    private val btAdapter: BluetoothAdapter = get()
    private val btLeScanner: BluetoothLeScanner = get()
    private val bleGatt: BleGatt = get()
    private val bleManager: BleManager = get()

    private val bleRepository: IBleRepository = get()

    private val dispatcher = UnconfinedTestDispatcher()
    private val application: Application = get()

    private val flow = flow {
        emit(deviceList)
    }


    companion object {

        @JvmStatic
        @BeforeAll
        fun setupTest() {
            loadKoinModules(endToEndModule)
        }
    }

    @BeforeEach
    fun setup() {
        println("Before each...")
        clearAllMocks()

        every { btAdapter.bluetoothLeScanner } returns btLeScanner
        every { btAdapter.isEnabled } returns true

        every { bleManager.userMessage } returns MutableStateFlow(null)
        every { bleManager.isScanning } returns MutableStateFlow(true)
        every { bleGatt.connectMessage } returns MutableStateFlow(ConnectionState.CONNECTED)
        every { bleGatt.deviceDetails } returns MutableStateFlow(deviceDetail.services)

        every { bleRepository.getScannedDevices(ScanFilterOption.FAVORITES) } returns flow {
            emit(deviceList.filter { it.favorite })
        }

    }

    @Test
    fun scanListLoaded() {

        every { bleRepository.getScannedDevices(null) } returns flow

        extension.runComposeTest {

            onAllNodes(hasText("Last seen:", substring = true)).assertCountEquals(3)
            val bitmap = onRoot().captureToImage().asAndroidBitmap()
            saveScreenshot(
                "homeScanListUnfiltered"
                        + System.currentTimeMillis().toString(), bitmap
            )
        }

    }

    @Test
    fun scanListFavorites() {

        every { bleRepository.getScannedDevices(null) } returns flow.onEach {
            it.filter { device -> device.favorite }
        }

        extension.runComposeTest {

            onNodeWithText("Favorites").performClick()

            onAllNodes(hasText("Last seen:", substring = true)).assertCountEquals(1)
            onNodeWithText("GOOD-RSSI").assertExists()

            val bitmap = onRoot().captureToImage().asAndroidBitmap()
            saveScreenshot(
                "homeScanListFavorites"
                        + System.currentTimeMillis().toString(), bitmap
            )
        }

    }

}