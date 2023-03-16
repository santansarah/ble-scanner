package com.santansarah.scan.presentation.components

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.santansarah.scan.R
import com.santansarah.scan.domain.models.BleConnectEvents
import com.santansarah.scan.domain.models.DeviceDetail
import com.santansarah.scan.domain.models.DeviceEvents
import com.santansarah.scan.domain.models.ScanUI
import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.local.entities.displayName
import com.santansarah.scan.presentation.previewparams.FeatureParams
import com.santansarah.scan.presentation.previewparams.LandscapeLayoutParams
import com.santansarah.scan.presentation.previewparams.LandscapeLayouts
import com.santansarah.scan.presentation.previewparams.PortraitLayoutParams
import com.santansarah.scan.presentation.previewparams.PortraitLayouts
import com.santansarah.scan.presentation.scan.device.DeviceButtons
import com.santansarah.scan.presentation.scan.device.DeviceMenu
import com.santansarah.scan.presentation.theme.SanTanScanTheme
import com.santansarah.scan.presentation.theme.appBarTitle
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarWithBackButton(
    appLayoutInfo: AppLayoutInfo,
    onBackClicked: () -> Unit,
    scanUi: ScanUI,
    deviceDetail: DeviceDetail,
    deviceEvents: DeviceEvents,
    bleConnectEvents: BleConnectEvents,
    onControlClick: (String) -> Unit
) {

    var deviceMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val connectEnabled = !scanUi.bleMessage.isActive()
    val disconnectEnabled = scanUi.bleMessage.isActive()

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
            if (appLayoutInfo.appLayoutMode.isLandscape()) {
                DeviceButtons(
                    connectEnabled = connectEnabled,
                    onConnect = bleConnectEvents.onConnect,
                    device = deviceDetail.scannedDevice,
                    disconnectEnabled = disconnectEnabled,
                    onDisconnect = bleConnectEvents.onDisconnect,
                    services = deviceDetail.services,
                    onControlClick = onControlClick
                )
            } else {
                Text(
                    text = deviceDetail.scannedDevice.displayName(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = appBarTitle
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                BackIcon(contentDesc = "Go Back")
            }
        },
        actions = {
            DeviceMenu(
                device = deviceDetail.scannedDevice,
                expanded = deviceMenuExpanded,
                onExpanded = { deviceMenuExpanded = it },
                onEdit = deviceEvents.onIsEditing,
                onFavorite = deviceEvents.onFavorite,
                onForget = deviceEvents.onForget
            )
        }
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeAppBar(
    scanning: Boolean,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onHelp: () -> Unit,
    permissionsGranted: Boolean
) {

    var homeMenuExpanded by rememberSaveable { mutableStateOf(false) }

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
        navigationIcon = {
            MainMenu(
                expanded = homeMenuExpanded,
                onExpanded = { homeMenuExpanded = it },
                onHelp = onHelp
            )
        },
        title = {
            Text(
                text = if (scanning) "Your Devices" else "Scan Stopped",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = appBarTitle
            )
        },
        actions = {
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.outline
                ),
                enabled = (permissionsGranted && !scanning),
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
                onClick = { onStopScan() },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.disconnect),
                        contentDescription = "Disconnect"
                    )
                })
        }
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BasicBackTopAppBar(
    appLayoutInfo: AppLayoutInfo,
    onBackClicked: () -> Unit,
    titleContent: @Composable () -> Unit
) {

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
        title = { titleContent() },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                BackIcon(contentDesc = "Go Back")
            }
        },
    )
}

@PortraitLayouts
@Composable
fun PreviewHomeBar() {
    SanTanScanTheme() {
        HomeAppBar(scanning = true, {}, {}, {}, true)
    }
}


@PortraitLayouts
@Composable
fun PreviewAppBar(
    @PreviewParameter(PortraitLayoutParams::class) featureParams: FeatureParams
) {
    val device = ScannedDevice(
        0, "ELK-BLEDOM", "24:A9:30:53:5A:97", -45,
        "Microsoft", listOf("Human Readable Device"),
        listOf("Windows 10 Desktop"), 0L,
        customName = null,
        baseRssi = 0, favorite = false, forget = false
    )
    SanTanScanTheme() {
        AppBarWithBackButton(
            appLayoutInfo = featureParams.appLayoutInfo,
            onBackClicked = { /*TODO*/ },
            scanUi = featureParams.scanState.scanUI,
            deviceDetail = featureParams.detail,
            deviceEvents = featureParams.scanState.deviceEvents,
            bleConnectEvents = featureParams.scanState.bleConnectEvents,
            onControlClick = {}
        )
    }
}

@LandscapeLayouts
@Composable
fun PreviewLandscapeAppBar(
    @PreviewParameter(LandscapeLayoutParams::class) featureParams: FeatureParams
) {
    val device = ScannedDevice(
        0, "ELK-BLEDOM", "24:A9:30:53:5A:97", -45,
        "Microsoft", listOf("Human Readable Device"),
        listOf("Windows 10 Desktop"), 0L,
        customName = null,
        baseRssi = 0, favorite = false, forget = false
    )
    SanTanScanTheme() {
        AppBarWithBackButton(
            appLayoutInfo = featureParams.appLayoutInfo,
            onBackClicked = { /*TODO*/ },
            scanUi = featureParams.scanState.scanUI,
            deviceDetail = featureParams.detail,
            deviceEvents = featureParams.scanState.deviceEvents,
            bleConnectEvents = featureParams.scanState.bleConnectEvents,
            onControlClick = {}
        )
    }
}


