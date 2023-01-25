package com.santansarah.blescanner.presentation.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.santansarah.blescanner.presentation.components.ShowPermissions
import com.santansarah.blescanner.utils.permissionsList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeRoute(
    vm: ScanViewModel = koinViewModel()
) {

    val devices = vm.deviceList.collectAsStateWithLifecycle()
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permissionsList)

    LaunchedEffect(key1 = multiplePermissionsState.allPermissionsGranted) {
        if (multiplePermissionsState.allPermissionsGranted) {
            vm.startScan()
        }
    }

    if (!multiplePermissionsState.allPermissionsGranted) {
        ShowPermissions(multiplePermissionsState)
    } else {

        LazyColumn() {
            items(devices.value) { device ->


                ScannedDevice(device = device, onClick = vm::connectToDevice)

                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }

}

@Preview
@Composable
fun ListPreview() {

}