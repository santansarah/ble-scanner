package com.santansarah.scan.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.santansarah.scan.R
import com.santansarah.scan.presentation.theme.SanTanScanTheme
import com.santansarah.scan.presentation.theme.pagerHeaders

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissions(
    paddingValues: PaddingValues,
    multiplePermissionsState: MultiplePermissionsState,
    onAboutClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = paddingValues.calculateTopPadding()
            ),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(2f)
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.get_started),
                            contentDescription = "Get Started Icon",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                        Text(
                            modifier = Modifier.padding(start = 6.dp),
                            text = "Get Started",
                            style = pagerHeaders,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .offset(y = (-1).dp),
                        painter = painterResource(id = R.drawable.unlock),
                        contentDescription = "Unlock Icon",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Divider(modifier = Modifier.padding(top = 8.dp))
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Before you can start scanning for BLE devices, first, " +
                            "you'll need to enable some Bluetooth and Location permissions.",

                    )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onClick = {
                        multiplePermissionsState.launchMultiplePermissionRequest()
                    }) {
                    Text(text = "Allow Permissions")
                }
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onClick = {
                        onAboutClick()
                    }) {
                    Text(text = "About & Help")
                }
            }
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PermissionsPreview() {
    SanTanScanTheme(dynamicColor = false) {
        Surface() {
            ShowPermissions(
                paddingValues = PaddingValues(0.dp),
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
                },
                {})
        }
    }
}