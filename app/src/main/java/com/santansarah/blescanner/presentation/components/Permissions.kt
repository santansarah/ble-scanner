package com.santansarah.blescanner.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissions(
    multiplePermissionsState: MultiplePermissionsState
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedCard(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Get Started",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    color = if (!isSystemInDarkTheme())
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Before you can start scanning for BLE devices, first, " +
                            "you'll need to enable some Bluetooth and Location permissions.",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    modifier = Modifier.padding(bottom = 16.dp),
                    onClick = {
                        multiplePermissionsState.launchMultiplePermissionRequest()
                    }) {
                    Text(text = "Allow Permissions")
                }
            }
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PermissionsPreview() {
    BLEScannerTheme(dynamicColor = false) {
        Surface() {
            ShowPermissions(
                multiplePermissionsState = object : MultiplePermissionsState {
                    override val allPermissionsGranted: Boolean
                        get() = TODO("Not yet implemented")
                    override val permissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val revokedPermissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val shouldShowRationale: Boolean
                        get() = TODO("Not yet implemented")

                    override fun launchMultiplePermissionRequest() {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}