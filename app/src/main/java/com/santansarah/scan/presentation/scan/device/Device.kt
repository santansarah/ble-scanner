package com.santansarah.scan.presentation.scan.device

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.santansarah.scan.local.entities.displayName
import com.santansarah.scan.domain.models.BleReadWriteCommands
import com.santansarah.scan.domain.models.ScanUI
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo

@Composable
fun ShowDeviceBody(
    appLayoutInfo: AppLayoutInfo,
    scanUi: ScanUI,
    bleReadWriteCommands: BleReadWriteCommands,
    onShowUserMessage: (String) -> Unit,
    onEdit: (Boolean) -> Unit,
    isEditing: Boolean,
    onSave: (String) -> Unit,
) {

    val scannedDevice = scanUi.selectedDevice!!.scannedDevice

    if (isEditing)
        EditDevice(onSave = onSave, updateEdit = onEdit, scannedDevice.displayName())
    else {
        ServicePager(
            appLayoutInfo = appLayoutInfo,
            selectedDevice = scanUi.selectedDevice,
            onRead = bleReadWriteCommands.onRead,
            onShowUserMessage = onShowUserMessage,
            onWrite = bleReadWriteCommands.onWrite,
            onReadDescriptor = bleReadWriteCommands.onReadDescriptor,
            onWriteDescriptor = bleReadWriteCommands.onWriteDescriptor
        )

    }
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
            modifier = Modifier.align(Alignment.TopEnd),
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
/*

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
}*/
