package com.santansarah.blescanner.presentation.scan.device

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDetail
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.presentation.components.AppBarWithBackButton
import com.santansarah.blescanner.presentation.scan.ScanState
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.presentation.theme.codeFont
import com.santansarah.blescanner.utils.ParsableCharacteristic
import com.santansarah.blescanner.utils.toDate
import timber.log.Timber

@Composable
fun ShowDevice(
    paddingValues: PaddingValues,
    scanState: ScanState,
    onConnect: (String) -> Unit,
    onDisconnect: () -> Unit,
    onBack: () -> Unit,
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit
) {

    val scannedDevice = scanState.selectedDevice!!.scannedDevice

    Column(
        modifier = Modifier
            .fillMaxSize()
        //.padding(horizontal = 8.dp)
    ) {

        AppBarWithBackButton(
            title = scannedDevice.deviceName ?: "Unknown",
            onBack
        )

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
            //.background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(modifier = Modifier.padding(6.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val connectEnabled = !(scanState.bleMessage == ConnectionState.CONNECTING ||
                            scanState.bleMessage == ConnectionState.CONNECTED)
                    val disconnectEnabled =
                        !(scanState.bleMessage == ConnectionState.DISCONNECTING ||
                                scanState.bleMessage == ConnectionState.DISCONNECTED)

                    val statusText = buildAnnotatedString {
                        append("Status: ")
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(scanState.bleMessage.toTitle())
                        }
                    }

                    Text(text = statusText,
                        color = MaterialTheme.colorScheme.onPrimary)

                    ConnectionStatus(
                        connectEnabled, onConnect,
                        scannedDevice, disconnectEnabled, onDisconnect
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                DeviceDetails(scannedDevice)
            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        ServicePager(scanState.selectedDevice, onRead, onShowUserMessage)
    }

}


@Composable
private fun DeviceDetails(device: ScannedDevice) {
    //Text(text = device.deviceName ?: "Unknown Name")
    device.manufacturer?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    device.extra?.let {
        Text(
            text = it.joinToString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    Text(
        text = device.address,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Text(
        text = "Last scanned: ${device.lastSeen.toDate()}",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun ConnectionStatus(
    connectEnabled: Boolean,
    onConnect: (String) -> Unit,
    device: ScannedDevice,
    disconnectEnabled: Boolean,
    onDisconnect: () -> Unit
) {
    Row() {
        FilledIconButton(
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            enabled = connectEnabled,
            onClick = { onConnect(device.address) },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.connect),
                    contentDescription = "Connect"
                )
            })
        FilledIconButton(
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            enabled = disconnectEnabled,
            onClick = { onDisconnect() },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.disconnect),
                    contentDescription = "Disconnect"
                )
            })
    }
}


@Composable
fun ReadWriteMenu(
    expanded: Boolean,
    onExpanded: (Boolean) -> Unit,
    char: DeviceCharacteristics,
    onState: (Int) -> Unit
) {
    Box(
        //modifier = Modifier.align(Alignment.End)
    ) {
        IconButton(
            //modifier = Modifier.offset((-14).dp),
            onClick = { onExpanded(true) }) {
            Icon(
                //modifier = Modifier.then(Modifier.padding(0.dp)),
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Actions"
            )
        }
        DropdownMenu(
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.primaryContainer
            ),
            expanded = expanded,
            onDismissRequest = { onExpanded(false) }
        ) {
            DropdownMenuItem(
                //modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                enabled = char.canRead,
                text = { Text("Read") },
                onClick = { onState(0) },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Read"
                    )
                })
            Divider()
            DropdownMenuItem(
                //modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                enabled = char.canWrite,
                text = { Text("Write") },
                onClick = { onState(1) },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null
                    )
                })
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
                    PaddingValues(4.dp),
                    ScanState(
                        emptyList(),
                        DeviceDetail(
                            scannedDevice =
                            ScannedDevice(
                                deviceId = 41,
                                deviceName = "EASYWAY-BLE",
                                address = "93:58:00:27:XX:00",
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
                                            readBytes = null
                                        ),
                                        DeviceCharacteristics(
                                            uuid = "00002a00-0000-1000-8000-00805f9b34fb",
                                            name = "Appearance",
                                            descriptor = null,
                                            permissions = 0,
                                            properties = 2,
                                            writeTypes = 2,
                                            descriptors = emptyList(),
                                            canRead = true,
                                            canWrite = false,
                                            readBytes = null
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
                                            canWrite = false, readBytes = byteArrayOf(-60, 3)
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
                                            readBytes = null
                                        ),
                                        DeviceCharacteristics(
                                            uuid = "0000ae02-0000-1000-8000-00805f9b34fb",
                                            name = "Mfr Characteristic",
                                            descriptor = null, permissions = 0, properties = 2,
                                            writeTypes = 2, descriptors = emptyList(),
                                            canRead = true, canWrite = false,
                                            readBytes = null
                                        ),
                                        DeviceCharacteristics(
                                            uuid = "0000ae03-0000-1000-8000-00805f9b34fb",
                                            name = "Mfr Characteristic",
                                            descriptor = null, permissions = 0, properties = 16,
                                            writeTypes = 2, descriptors = emptyList(),
                                            canRead = false, canWrite = false,
                                            readBytes = null,
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
                    {},
                    {}
                )
            }
        }
    }
}