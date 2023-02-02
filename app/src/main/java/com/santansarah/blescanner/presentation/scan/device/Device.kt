package com.santansarah.blescanner.presentation.scan.device

import android.content.res.Configuration
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDetail
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.presentation.scan.ScanState
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.toDate
import timber.log.Timber

@Composable
fun ShowDevice(
    scanState: ScanState,
    onConnect: (String) -> Unit,
    onDisconnect: () -> Unit,
    onBack: () -> Unit,
    onRead: (String) -> Unit
) {

    val device = scanState.selectedDevice!!.scannedDevice

    Column(
        //modifier = Modifier.fillMaxSize()
    ) {

        Column {
            AppBarWithBackButton(title = "Device Detail", onBack)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Status: ${scanState.bleMessage.toTitle()}")

            if (scanState.bleMessage == ConnectionState.DISCONNECTED ||
                scanState.bleMessage == ConnectionState.DISCONNECTING
            ) {
                Button(onClick = { onConnect }) {
                    Text(text = "Reconnect")
                }
            } else {
                Button(onClick = { onDisconnect }) {
                    Text(text = "Disconnect")
                }
            }
        }

    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(text = device.deviceName ?: "Unknown Name")
    device.manufacturer?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    device.extra?.let {
        Text(
            text = it.joinToString(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Text(
        text = device.address,
        style = MaterialTheme.typography.bodyMedium
    )
    Text(
        text = "Last scanned: ${device.lastSeen.toDate()}",
        style = MaterialTheme.typography.labelSmall
    )

    Spacer(modifier = Modifier.height(24.dp))

    Timber.d("deviceFromComposable: ${scanState.selectedDevice}")

    if (scanState.selectedDevice.services.isNotEmpty()) {

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            scanState.selectedDevice.services.forEach {

                OutlinedCard(
                    //modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = it.uuid,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Divider(
                            Modifier.padding(top = 4.dp),
                            2.dp, MaterialTheme.colorScheme.outline
                        )

                        Column(modifier = Modifier.padding(10.dp)) {

                            it.characteristics.forEach { char ->
                                Text(
                                    text = char.name,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = char.uuid,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (char.canRead) {
                                    Button(onClick = { onRead(char.uuid) }) {
                                        Text(text = "Read")
                                    }
                                }
                                Text(text = char.readValue ?: "")
                                Text(text = "Write: ${char.canWrite}")


                                if (it.characteristics.indexOf(char) < it.characteristics.count() - 1) {
                                    Spacer(modifier = Modifier.height(26.dp))
                                }


                                /*char.descriptors.forEach { desc ->
                            Text(text = "- ${desc.uuid}")
                            Text(text = desc.permissions.toString())
                        }*/
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

        }

    }

}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarWithBackButton(
    title: String,
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                BackIcon(contentDesc = "Go Back")
            }
        }
    )
}

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
    contentDesc: String
) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = contentDesc
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun previewDeviceDetail() {
    val device = ScannedDevice(
        0, "ELK-BLEDOM", "24:A9:30:53:5A:97", -45,
        "Microsoft", listOf("Human Readable Device"),
        listOf("Windows 10 Desktop"), 0L
    )
    BLEScannerTheme {
        Surface() {
            val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
            Column(
                modifier = Modifier.padding(
                    start = 14.dp,
                    end = 14.dp,
                    top = systemBarsPadding.calculateTopPadding() + 14.dp,
                    bottom = systemBarsPadding.calculateBottomPadding() + 14.dp
                )
            ) {
                ShowDevice(
                    ScanState(
                        emptyList(),
                        DeviceDetail(
                            scannedDevice =
                            ScannedDevice(
                                deviceId = 41,
                                deviceName = "EASYWAY-BLE",
                                address = "93:58:44:27:C9:AC",
                                rssi = -93,
                                manufacturer = "Ericsson Technology Licensing",
                                services = listOf("Heart Rate"),
                                extra = null,
                                lastSeen = 1675293173796
                            ),
                            services = listOf(
                                DeviceService(
                                    uuid = "1800",
                                    name = "Generic Access",
                                    characteristics = listOf(
                                        DeviceCharacteristics(
                                            uuid = "00002a00-0000-1000-8000-00805f9b34fb",
                                            name = "Device Name",
                                            descriptor = null,
                                            permissions = 0,
                                            properties = 2,
                                            writeTypes = 2,
                                            descriptors = emptyList(),
                                            canRead = true,
                                            canWrite = false,
                                            readValue = null
                                        )
                                    )
                                ),
                                DeviceService(
                                    uuid = "1801",
                                    name = "Generic Attribute",
                                    characteristics = listOf(
                                        DeviceCharacteristics(
                                            uuid = "00002a05-0000-1000-8000-00805f9b34fb",
                                            name = "Service Changed",
                                            descriptor = null,
                                            permissions = 0, properties = 2, writeTypes = 2,
                                            descriptors = emptyList(), canRead = true,
                                            canWrite = false, readValue = null
                                        )
                                    )
                                ),
                                DeviceService(
                                    uuid = "0000ae00-0000-1000-8000-00805f9b34fb",
                                    name = "Mfr Service",
                                    characteristics = listOf(
                                        DeviceCharacteristics(
                                            uuid = "0000ae01-0000-1000-8000-00805f9b34fb",
                                            name = "Mfr Characteristic",
                                            descriptor = null,
                                            permissions = 0,
                                            properties = 8,
                                            writeTypes = 2,
                                            descriptors = emptyList(),
                                            canRead = false,
                                            canWrite = true,
                                            readValue = null
                                        ),
                                        DeviceCharacteristics(
                                            uuid = "0000ae02-0000-1000-8000-00805f9b34fb",
                                            name = "Mfr Characteristic",
                                            descriptor = null, permissions = 0, properties = 2,
                                            writeTypes = 2, descriptors = emptyList(),
                                            canRead = true, canWrite = false, readValue = null
                                        ),
                                        DeviceCharacteristics(
                                            uuid = "0000ae03-0000-1000-8000-00805f9b34fb",
                                            name = "Mfr Characteristic",
                                            descriptor = null, permissions = 0, properties = 16,
                                            writeTypes = 2, descriptors = emptyList(),
                                            canRead = false, canWrite = false, readValue = null
                                        )
                                    )
                                )
                            )
                        ),
                        ConnectionState.CONNECTING,
                        null
                    ),
                    {},
                    {},
                    {},
                    {}
                )
            }
        }
    }
}