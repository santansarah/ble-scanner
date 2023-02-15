package com.santansarah.blescanner.presentation.scan

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.presentation.components.AppBarWithBackButton
import com.santansarah.blescanner.presentation.components.HomeAppBar
import com.santansarah.blescanner.presentation.components.ShowPermissions
import com.santansarah.blescanner.presentation.scan.device.ShowDevice
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.permissionsList
import kotlinx.coroutines.flow.asStateFlow
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    vm: ScanViewModel = koinViewModel()
) {

    val scanState = vm.scanState.collectAsStateWithLifecycle().value
    val scannerMessage = vm.scannerMessage.collectAsStateWithLifecycle().value
    val devices = scanState.devices
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permissionsList)
    val isScanning = vm.isScanning.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = multiplePermissionsState.allPermissionsGranted) {
        if (multiplePermissionsState.allPermissionsGranted) {
            vm.startScan()
        } else
            vm.stopScan()
    }

    val appSnackBarHostState = remember { SnackbarHostState() }
    scanState.userMessage?.let { userMessage ->
        LaunchedEffect(scanState.userMessage, userMessage) {
            appSnackBarHostState.showSnackbar(userMessage)
            vm.userMessageShown()
        }
    }

    scannerMessage?.let { scanningMessage ->
        LaunchedEffect(scanningMessage) {
            appSnackBarHostState.showSnackbar(scanningMessage)
            vm.scannerMessageShown()
        }
    }

    Scaffold(
        // modifier = Modifier.border(2.dp, Color.Magenta),
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(hostState = appSnackBarHostState) },
        topBar = {
            if (scanState.selectedDevice == null)
                HomeAppBar(
                    scanning = isScanning,
                    onStartScan = vm::startScan,
                    onStopScan = vm::stopScan
                )
            else
                AppBarWithBackButton(
                    title = scanState.selectedDevice.scannedDevice.deviceName ?: "Unknown",
                    vm::onBackFromDevice
                )
        }
    ) { padding ->

        if (!multiplePermissionsState.allPermissionsGranted) {
            ShowPermissions(multiplePermissionsState)
        } else {
            if (scanState.selectedDevice == null)
                ScannedDeviceList(
                    devices = devices, onClick = vm::onConnect, paddingValues = padding,
                )
            else
                ShowDevice(
                    paddingValues = padding,
                    scanState = scanState,
                    onConnect = vm::onConnect,
                    onDisconnect = vm::onDisconnect,
                    onRead = vm::readCharacteristic,
                    onShowUserMessage = vm::showUserMessage,
                    onWrite = vm::onWriteCharacteristic,
                    onReadDescriptor = vm::readDescriptor,
                    onWriteDescriptor = vm::writeDescriptor
                )
        }
    }

}

@Composable
private fun ScannedDeviceList(
    devices: List<ScannedDevice>,
    onClick: (String) -> Unit,
    paddingValues: PaddingValues,
) {

    LazyColumn(
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding() + 8.dp,
                start = 8.dp, end = 8.dp
            )
    ) {
        items(devices) { device ->

            ScannedDevice(device = device, onClick = onClick)
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ListPreview() {

    val deviceList = listOf(
        ScannedDevice(
            deviceId = 0,
            deviceName = "ELK-BLEDOM",
            address = "BE:00:FA:00:XX:00",
            rssi = -77,
            manufacturer = null,
            services = listOf("[Human Interface Device"),
            extra = null,
            lastSeen = 1674510398719
        ),
        ScannedDevice(
            deviceId = 0,
            deviceName = "EASYWAY-BLE",
            address = "93:00:44:00:XX:AC",
            rssi = -81,
            manufacturer = "Ericsson Technology Licensing",
            services = null,
            extra = null,
            lastSeen = 1674510397416
        )
    )

    BLEScannerTheme {
        Surface() {
            ScannedDeviceList(devices = deviceList, onClick = {}, PaddingValues(4.dp))
        }
    }

}