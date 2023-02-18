package com.santansarah.blescanner.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.displayName
import com.santansarah.blescanner.presentation.scan.device.DeviceMenu
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarWithBackButton(
    onBackClicked: () -> Unit,
    device: ScannedDevice,
    onEdit: (Boolean) -> Unit,
    onFavorite: (ScannedDevice) -> Unit,
    onForget: (ScannedDevice) -> Unit
) {

    var deviceMenuExpanded by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        //modifier = Modifier.border(2.dp, Color.Blue),
        windowInsets = WindowInsets(
            top = 0.dp,
            bottom = 0.dp
        ),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF00005d),
            titleContentColor = Color(0xFFcaccd9),
            navigationIconContentColor = MaterialTheme.colorScheme
                .onPrimary.copy(.7f)
        ),
        title = {
            Text(
                text = device.displayName(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                BackIcon(contentDesc = "Go Back")
            }
        },
        actions = {
            DeviceMenu(
                device = device,
                expanded = deviceMenuExpanded,
                onExpanded = {deviceMenuExpanded = it},
                onEdit = onEdit,
                onFavorite = onFavorite,
                onForget = onForget
            )
        }
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeAppBar(
    scanning: Boolean,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {

    TopAppBar(
        //modifier = Modifier.border(2.dp, Color.Blue),
        windowInsets = WindowInsets(
            top = 0.dp,
            bottom = 0.dp
        ),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF00005d),
            titleContentColor = Color(0xFFcaccd9),
            navigationIconContentColor = MaterialTheme.colorScheme
                .onPrimary.copy(.7f)
        ),
        title = {
            Text(
                text = if (scanning) "Your Devices" else "Scan Stopped",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                //style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.outline
                ),
                enabled = !scanning,
                onClick = { onStartScan() },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.connect),
                        contentDescription = "Connect"
                    )
                })
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.outline
                ),
                enabled = scanning,
                onClick = { onStopScan()  },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.disconnect),
                        contentDescription = "Disconnect"
                    )
                })
        }
    )
}



@Preview
@Composable
fun PreviewAppBar() {
    val device = ScannedDevice(
        0, "ELK-BLEDOM", "24:A9:30:53:5A:97", -45,
        "Microsoft", listOf("Human Readable Device"),
        listOf("Windows 10 Desktop"), 0L,
        customName = null,
        baseRssi = 0,favorite = false, forget = false
    )
    BLEScannerTheme() {
        AppBarWithBackButton({},device,
            {}, {}, {})
    }
}

@Preview
@Composable
fun PreviewHomeBar() {
    BLEScannerTheme() {
        HomeAppBar(scanning = true, {}, {})
    }
}

