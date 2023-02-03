package com.santansarah.blescanner.presentation.scan.device

import android.content.res.Configuration
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
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
import com.santansarah.blescanner.presentation.scan.ScanState
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.presentation.theme.codeFont
import com.santansarah.blescanner.utils.bits
import com.santansarah.blescanner.utils.bitsToHex
import com.santansarah.blescanner.utils.decodeSkipUnreadable
import com.santansarah.blescanner.utils.print
import com.santansarah.blescanner.utils.toBinaryString
import com.santansarah.blescanner.utils.toDate
import com.santansarah.blescanner.utils.toHex
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
        modifier = Modifier.fillMaxSize()
    ) {

        AppBarWithBackButton(
            title = device.deviceName ?: "Unknown",
            onBack
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val connectDisabled = !(scanState.bleMessage == ConnectionState.CONNECTING ||
                    scanState.bleMessage == ConnectionState.CONNECTED)
            val disconnectDisabled = !(scanState.bleMessage == ConnectionState.DISCONNECTING ||
                    scanState.bleMessage == ConnectionState.DISCONNECTED)

            val statusText = buildAnnotatedString {
                append("Status: ")
                withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(scanState.bleMessage.toTitle())
                }
            }

            Text(text = statusText)

            Row() {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    enabled = connectDisabled,
                    onClick = { onConnect(device.address) },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.connect),
                            contentDescription = "Connect"
                        )
                    })
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = disconnectDisabled,
                    onClick = { onDisconnect() },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.disconnect),
                            contentDescription = "Disconnect"
                        )
                    })
            }


        }


        Spacer(modifier = Modifier.height(10.dp))

        //Text(text = device.deviceName ?: "Unknown Name")
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

        if (scanState.selectedDevice.services.isNotEmpty()) {
            val services = scanState.selectedDevice.services
            val totalServices by rememberSaveable { mutableStateOf(services.count()) }
            var currentService by rememberSaveable { mutableStateOf(0) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    enabled = (currentService > 0),
                    onClick = {
                        currentService--
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Next Service"
                    )
                }
                Text(text = "Services")
                IconButton(
                    enabled = (currentService != (totalServices-1)),
                    onClick = {
                        currentService++
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Service"
                    )
                }
            }

            ServicePagerDetail(services[currentService], onRead)

        }
    }

}

@Composable
private fun ServicePagerDetail(
    service: DeviceService,
    onRead: (String) -> Unit
) {

        OutlinedCard(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = service.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = service.uuid,
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider(
                    Modifier.padding(top = 4.dp),
                    2.dp, MaterialTheme.colorScheme.outline
                )

                Column(modifier = Modifier.padding(10.dp)) {

                    service.characteristics.forEach { char ->
                        Text(
                            text = char.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = char.uuid,
                            style = MaterialTheme.typography.bodySmall
                        )

                        FilledIconButton(
                            onClick = { onRead(char.uuid) },
                            enabled = char.canRead
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.outline_arrow_circle_down
                                ),
                                contentDescription = "Read Data"
                            )
                        }

                        char.readBytes?.let {bytes ->
                            SelectionContainer {
                                Column {
                                    Text(
                                        text = "String, Hex, Bytes, Binary, Bits, Bit Hex:",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        text = bytes.decodeSkipUnreadable(),
                                        style = codeFont
                                    )
                                    Text(
                                        text = ("0x" + bytes.toHex()),
                                        style = codeFont
                                    )
                                    Text(
                                        text = "[" + bytes.print() + "]",
                                        style = codeFont
                                    )
                                    Text(
                                        text = bytes.toBinaryString(),
                                        style = codeFont
                                    )
                                    Text(
                                        text = bytes.bits(),
                                        style = codeFont
                                    )
                                    Text(
                                        text = bytes.bitsToHex(),
                                        style = codeFont
                                    )
                                }
                            }
                        }

                        Text(text = "Write: ${char.canWrite}")

                        if (service.characteristics.indexOf(char) < service.characteristics.count() - 1) {
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

}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarWithBackButton(
    title: String,
    onBackClicked: () -> Unit
) {

    CenterAlignedTopAppBar(
        //modifier = Modifier.border(2.dp, Color.Blue),
        windowInsets = WindowInsets(
            top = 0.dp,
            bottom = 0.dp
        ),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
                    {}
                )
            }
        }
    }
}