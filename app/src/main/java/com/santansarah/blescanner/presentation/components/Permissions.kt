package com.santansarah.blescanner.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.santansarah.blescanner.utils.permissionsList

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissions() {

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permissionsList)

    if (!multiplePermissionsState.allPermissionsGranted) {

        Column {
            Text(text = "Welcome. Before you can start scanning for BLE devices, first, you'll need to grant some permissions.")

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                multiplePermissionsState.launchMultiplePermissionRequest()
            }) {
                Text(text = "Allow Permissions")
            }
        }
    }

    LaunchedEffect(key1 = multiplePermissionsState.allPermissionsGranted) {
        if (multiplePermissionsState.allPermissionsGranted) {

        }
    }

}