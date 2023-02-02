package com.santansarah.blescanner.presentation.scan

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.presentation.components.ShowPermissions
import com.santansarah.blescanner.presentation.scan.device.ShowDevice
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.permissionsList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeRoute(
    vm: ScanViewModel = koinViewModel()
) {

    val scanState = vm.scanState.collectAsStateWithLifecycle().value
    val devices = scanState.devices
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permissionsList)

    LaunchedEffect(key1 = multiplePermissionsState.allPermissionsGranted) {
        if (multiplePermissionsState.allPermissionsGranted) {
            vm.startScan()
        }
    }

    if (!multiplePermissionsState.allPermissionsGranted) {
        ShowPermissions(multiplePermissionsState)
    } else {
        if (scanState.selectedDevice == null)
            ScannedDeviceList(devices, vm::onConnect)
        else
            ShowDevice(
                scanState = scanState,
                onConnect = vm::onConnect,
                onDisconnect = vm::onDisconnect,
                onBack = vm::onBackFromDevice,
                onRead = vm::readCharacteristic)
    }

}

@Composable
private fun ScannedDeviceList(
    devices: List<ScannedDevice>,
    onClick: (String) -> Unit
) {
    LazyColumn() {
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
            ScannedDeviceList(devices = deviceList, onClick = {})
        }
    }

}