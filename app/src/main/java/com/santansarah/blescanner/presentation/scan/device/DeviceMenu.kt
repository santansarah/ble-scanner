package com.santansarah.blescanner.presentation.scan.device

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.presentation.previewparams.ScannedDevicePreviewParameterProvider
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme

@Composable
fun DeviceMenu(
    device: ScannedDevice,
    expanded: Boolean,
    onExpanded: (Boolean) -> Unit,
    onEdit: (Boolean) -> Unit,
    onFavorite: (ScannedDevice) -> Unit,
    onForget: (ScannedDevice) -> Unit
) {
    IconButton(
        //modifier = Modifier.offset((-14).dp),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.outline
        ),
        onClick = { onExpanded(true) }) {
        Icon(
            //modifier = Modifier.then(Modifier.padding(0.dp)),
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Actions",
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
            enabled = device.deviceName != null,
            text = { Text("Edit") },
            onClick = {
                onExpanded(false)
                onEdit(true)
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Edit"
                )
            })
        Divider(color = MaterialTheme.colorScheme.primaryContainer)
        val favoriteIcon = if (device.favorite) R.drawable.favorite_selected
        else R.drawable.favorite_unselected

        DropdownMenuItem(
            //modifier = Modifier.background(MaterialTheme.colorScheme.primary),
            //enabled = char.canWrite,
            text = { Text("Favorite") },
            onClick = {
                onExpanded(false)
                onFavorite(device)
            },
            leadingIcon = {
                Icon(
                    painterResource(id = favoriteIcon),
                    contentDescription = null
                )
            })
        Divider(color = MaterialTheme.colorScheme.primaryContainer)
        DropdownMenuItem(
            //modifier = Modifier.background(MaterialTheme.colorScheme.primary),
            //enabled = char.canWrite,
            text = { Text("Forget") },
            onClick = {
                onExpanded(false)
                onForget(device)
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.delete_forever),
                    contentDescription = null
                )
            })
    }
}
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true,
    showSystemUi = true
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showSystemUi = true, showBackground = true
)
@Composable
fun PreviewDeviceMenu(
    @PreviewParameter(ScannedDevicePreviewParameterProvider::class) device: ScannedDevice
) {

    //var state by rememberSaveable { mutableStateOf(0) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    BLEScannerTheme {
        Surface(
            modifier = Modifier.size(200.dp)
        ) {
            DeviceMenu(
                device = device,
                expanded = expanded,
                onExpanded = {expanded = it},
                onEdit = {},
                onFavorite = {},
                onForget = {}
            )
        }
    }
}
