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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.displayName
import com.santansarah.blescanner.domain.bleparsables.ELKBLEDOM
import com.santansarah.blescanner.domain.models.BleProperties
import com.santansarah.blescanner.domain.models.BleWriteTypes
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDetail
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.domain.models.ScanState
import com.santansarah.blescanner.presentation.previewparams.PreviewDeviceDetailProvider
import com.santansarah.blescanner.presentation.previewparams.ScannedDevicePreviewParameterProvider
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.toDate

@Composable
fun ShowDevice(
    paddingValues: PaddingValues,
    scanState: ScanState,
    onConnect: (String) -> Unit,
    onDisconnect: () -> Unit,
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit,
    onWrite: (String, String) -> Unit,
    onReadDescriptor: (String, String) -> Unit,
    onWriteDescriptor: (String, String, String) -> Unit,
    onEdit: (Boolean) -> Unit,
    isEditing: Boolean,
    onSave: (String) -> Unit,
    onControlClick: (String) -> Unit
) {

    val scannedDevice = scanState.selectedDevice!!.scannedDevice

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        //.padding(horizontal = 8.dp)
    ) {

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

                    Text(
                        text = statusText,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    DeviceButtons(
                        connectEnabled = connectEnabled,
                        onConnect = onConnect,
                        device = scannedDevice,
                        disconnectEnabled = disconnectEnabled,
                        onDisconnect = onDisconnect,
                        services = scanState.selectedDevice.services,
                        onControlClick = onControlClick,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                DeviceDetails(scannedDevice)
            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isEditing)
            EditDevice(onSave = onSave, updateEdit = onEdit, scannedDevice.displayName())
        else {
            ServicePager(
                selectedDevice = scanState.selectedDevice,
                onRead = onRead,
                onShowUserMessage = onShowUserMessage,
                onWrite = onWrite,
                onReadDescriptor = onReadDescriptor,
                onWriteDescriptor = onWriteDescriptor
            )

        }
    }

}


@Composable
fun DeviceDetails(device: ScannedDevice) {
    //Text(text = device.deviceName ?: "Unknown Name")
    device.manufacturer?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
    device.extra?.let {
        Text(
            text = it.joinToString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
    Text(
        text = device.address,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
    Text(
        text = "Last scanned: ${device.lastSeen.toDate()}",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun DeviceButtons(
    connectEnabled: Boolean,
    onConnect: (String) -> Unit,
    device: ScannedDevice,
    disconnectEnabled: Boolean,
    onDisconnect: () -> Unit,
    services: List<DeviceService>,
    onControlClick: (String) -> Unit,
) {
    Row() {
        ConnectButtons(connectEnabled, onConnect, device, disconnectEnabled, onDisconnect)
        services.flatMap { it.characteristics }.find {
            it.uuid == ELKBLEDOM.uuid
        }?.also {
            FilledIconButton(
                /*colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color(0xFF0087ff),
                    contentColor = Color(0xFFe2e2e9)
                ),*/
                onClick = { onControlClick(device.address) },
                content = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.control),
                        contentDescription = "Disconnect"
                    )
                })
        }
    }
}

@Composable
fun ConnectButtons(
    connectEnabled: Boolean,
    onConnect: (String) -> Unit,
    device: ScannedDevice,
    disconnectEnabled: Boolean,
    onDisconnect: () -> Unit
) {
    FilledIconButton(
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(.3f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
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
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(.3f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
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

@Composable
fun ReadWriteMenu(
    expanded: Boolean,
    onExpanded: (Boolean) -> Unit,
    onState: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
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
                //enabled = char.canRead,
                text = { Text("Read") },
                onClick = {
                    onState(0)
                    onExpanded(false)
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Read"
                    )
                })
            Divider(color = MaterialTheme.colorScheme.primaryContainer)
            DropdownMenuItem(
                //modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                //enabled = char.canWrite,
                text = { Text("Write") },
                onClick = {
                    onState(1)
                    onExpanded(false)
                },
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
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF17191b
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc0e5dd
)
@Composable
fun PreviewDeviceDetail(
    @PreviewParameter(PreviewDeviceDetailProvider::class) deviceDetail: DeviceDetail
) {
    BLEScannerTheme {

        ShowDevice(
            PaddingValues(4.dp),
            ScanState(
                emptyList(),
                deviceDetail,
                ConnectionState.CONNECTING,
                null,
                null
            ),
            {},
            {},
            {},
            {},
            { _: String, _: String -> },
            { _: String, _: String -> },
            { _: String, _: String, _: String -> },
            {},
            false,
            {},
            {}
        )
    }
}