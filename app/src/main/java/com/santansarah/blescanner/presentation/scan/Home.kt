package com.santansarah.blescanner.presentation.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.santansarah.blescanner.utils.permissionsList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeRoute(
    vm: ScanViewModel = koinViewModel(),
    isScanning: Boolean
) {

    val devices = vm.deviceList.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = (isScanning).toString(),
        style = MaterialTheme.typography.headlineLarge)

    }
    
    LazyColumn() {
        items(devices.value) { device ->

            ScannedDevice(device = device)
            
            Spacer(modifier = Modifier.height(10.dp))

        }
    }

}