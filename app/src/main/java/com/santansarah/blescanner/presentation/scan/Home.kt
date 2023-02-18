package com.santansarah.blescanner.presentation.scan

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.domain.models.ScanFilterOption
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

    var isEditing by rememberSaveable { mutableStateOf(false) }

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
                    onBackClicked = vm::onBackFromDevice,
                    device = scanState.selectedDevice.scannedDevice,
                    onEdit = { isEditing = it},
                    onFavorite = vm::onFavorite,
                    onForget = vm::onForget
                )
        }
    ) { padding ->

        if (!multiplePermissionsState.allPermissionsGranted) {
            ShowPermissions(multiplePermissionsState)
        } else {
            if (scanState.selectedDevice == null) {
                DeviceListScreen(
                    devices = devices, onClick = vm::onConnect, paddingValues = padding,
                    onFilter = vm::onFilter, scanFilterOption = scanState.scanFilterOption,
                    onFavorite = vm::onFavorite, onForget = vm::onForget
                )
            }
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
                    onWriteDescriptor = vm::writeDescriptor,
                    onEdit = { isEditing = it},
                    isEditing = isEditing,
                    onSave = vm::onNameChange
                )
        }
    }

}

@Composable
fun DeviceListScreen(
    paddingValues: PaddingValues,
    devices: List<ScannedDevice>,
    onClick: (String) -> Unit,
    onFilter: (ScanFilterOption?) -> Unit,
    scanFilterOption: ScanFilterOption?,
    onFavorite: (ScannedDevice) -> Unit,
    onForget: (ScannedDevice) -> Unit
) {
    Column(modifier = Modifier.padding(
        top = paddingValues.calculateTopPadding())){

        ScanFilters(onFilter = onFilter, scanFilterOption = scanFilterOption)
        ScannedDeviceList(devices, onClick, onFavorite, onForget)

    }
}

@Composable
private fun ScannedDeviceList(
    devices: List<ScannedDevice>,
    onClick: (String) -> Unit,
    onFavorite: (ScannedDevice) -> Unit,
    onForget: (ScannedDevice) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(devices) { device ->

            ScannedDevice(device = device, onClick = onClick, onFavorite = onFavorite,
            onForget = onForget)
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
            ScannedDeviceList(devices = deviceList, onClick = {},{}, {})
        }
    }

}