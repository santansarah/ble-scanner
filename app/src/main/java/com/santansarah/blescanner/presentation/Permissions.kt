package com.santansarah.blescanner.presentation

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.santansarah.blescanner.utils.permissionsList


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BLEPermissions() {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permissionsList)

    when {
        multiplePermissionsState.shouldShowRationale -> {
            //Text(text = " Permission ShouldShowRationale")
        }

        !multiplePermissionsState.allPermissionsGranted && !multiplePermissionsState.shouldShowRationale -> {
            /*Text(
                text = "Permission permanently denied ,you can enable it by going to app setting"
            )*/
        }
    }

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
}
