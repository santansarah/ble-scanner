package com.santansarah.blescanner.presentation.scan

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.permissionsList
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    vm: ScanViewModel = koinViewModel(),
    onControlClick: (String) -> Unit,
    appLayoutInfo: AppLayoutInfo
) {

    val scanState = vm.scanState.collectAsStateWithLifecycle().value
    val scannerMessage = vm.scannerMessage.collectAsStateWithLifecycle().value
    val devices = scanState.scanUI.devices
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permissionsList)
    val isScanning = vm.isScanning.collectAsStateWithLifecycle().value
    val isEditing = vm.isEditing.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = multiplePermissionsState.allPermissionsGranted) {
        if (multiplePermissionsState.allPermissionsGranted) {
            vm.startScan()
        } else
            vm.stopScan()
    }

    val appSnackBarHostState = remember { SnackbarHostState() }
    scanState.scanUI.userMessage?.let { userMessage ->
        LaunchedEffect(scanState.scanUI.userMessage, userMessage) {
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

    val selectedDevice = scanState.scanUI.selectedDevice

    Scaffold(
        // modifier = Modifier.border(2.dp, Color.Magenta),
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(hostState = appSnackBarHostState) },
        topBar = {
            if (selectedDevice == null)
                HomeAppBar(
                    scanning = isScanning,
                    onStartScan = vm::startScan,
                    onStopScan = vm::stopScan
                )
            else
                if (!appLayoutInfo.appLayoutMode.isLandscape())
                    AppBarWithBackButton(
                        appLayoutInfo = appLayoutInfo,
                        onBackClicked = vm::onBackFromDevice,
                        scanUi = scanState.scanUI,
                        deviceDetail = selectedDevice,
                        deviceEvents = scanState.deviceEvents,
                        bleConnectEvents = scanState.bleConnectEvents,
                        onControlClick = onControlClick
                    )
        }
    ) { padding ->

        if (!multiplePermissionsState.allPermissionsGranted) {
            ShowPermissions(multiplePermissionsState)
        } else {
            if (scanState.scanUI.selectedDevice == null) {
                DeviceListScreen(
                    devices = devices,
                    onClick = vm::onConnect,
                    paddingValues = padding,
                    onFilter = vm::onFilter,
                    scanFilterOption = scanState.scanUI.scanFilterOption,
                    onFavorite = vm::onFavorite,
                    onForget = vm::onForget,
                    appLayoutInfo = appLayoutInfo
                )
            } else {

                val homePadding = if (appLayoutInfo.appLayoutMode.isLandscape()) 0.dp
                else
                    padding.calculateTopPadding()

                Column(
                    modifier = Modifier
                        .padding(top = homePadding)
                        .fillMaxSize()
                    //.padding(horizontal = 8.dp)
                ) {
                    HomeScreen(
                        appLayoutInfo = appLayoutInfo,
                        scanState = scanState,
                        onControlClick = onControlClick,
                        onShowUserMessage = vm::showUserMessage,
                        deviceEvents = scanState.deviceEvents,
                        isEditing = isEditing.value,
                        onBackClicked = vm::onBackFromDevice,
                        onSave = vm::onNameChange
                    )

                }

            }
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
            lastSeen = 1674510398719,
            customName = null,
            baseRssi = 0,
            favorite = false,
            forget = false
        ),
        ScannedDevice(
            deviceId = 0,
            deviceName = "EASYWAY-BLE",
            address = "93:00:44:00:XX:AC",
            rssi = -81,
            manufacturer = "Ericsson Technology Licensing",
            services = null,
            extra = null,
            lastSeen = 1674510397416,
            customName = null,
            baseRssi = 0,
            favorite = false,
            forget = false
        )
    )

    BLEScannerTheme {
        Surface() {
            ScannedDeviceList(devices = deviceList, onClick = {}, {}, {})
        }
    }

}