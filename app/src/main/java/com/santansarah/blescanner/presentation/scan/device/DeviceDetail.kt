package com.santansarah.blescanner.presentation.scan.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.data.local.entities.displayName
import com.santansarah.blescanner.domain.models.ScanState
import com.santansarah.blescanner.presentation.components.AppBarWithBackButton
import com.santansarah.blescanner.presentation.previewparams.FeatureParams
import com.santansarah.blescanner.presentation.previewparams.LandscapePreviewParams
import com.santansarah.blescanner.presentation.previewparams.LandscapeThemePreviews
import com.santansarah.blescanner.presentation.previewparams.PortraitPreviewParams
import com.santansarah.blescanner.presentation.previewparams.ThemePreviews
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.toDate
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo

@Composable
fun ShowDeviceDetail(
    scanState: ScanState,
    onControlClick: (String) -> Unit,
    appLayoutInfo: AppLayoutInfo,
    onBackClicked: () -> Unit,
) {

    val scanUi = scanState.scanUI
    val scannedDevice = scanUi.selectedDevice!!.scannedDevice
    val services = scanUi.selectedDevice!!.services

    val connectEnabled = !scanUi.bleMessage.isActive()
    val disconnectEnabled = scanUi.bleMessage.isActive()

    val statusText = buildAnnotatedString {
        append("Status: ")
        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
            append(scanUi.bleMessage.toTitle())
        }
    }

    var modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
    if (appLayoutInfo.appLayoutMode.isLandscape()) {
        modifier = modifier.fillMaxHeight()
        //.fillMaxWidth(.3f)
    }

    Column(
        modifier = modifier
    ) {

        if (appLayoutInfo.appLayoutMode.isLandscape()) {

            AppBarWithBackButton(
                appLayoutInfo = appLayoutInfo,
                onBackClicked = onBackClicked,
                scanUi = scanState.scanUI,
                deviceDetail = scanState.scanUI.selectedDevice!!,
                deviceEvents = scanState.deviceEvents,
                bleConnectEvents = scanState.bleConnectEvents,
                onControlClick = onControlClick
            )
        }

        Column(modifier = Modifier.padding(6.dp)) {

            if (appLayoutInfo.appLayoutMode.isLandscape()) {

                Text(
                    text = scannedDevice.displayName(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = statusText,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(modifier = Modifier.height(10.dp))

                DeviceDetails(scannedDevice)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = statusText,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    DeviceButtons(
                        connectEnabled = connectEnabled,
                        onConnect = scanState.bleConnectEvents.onConnect,
                        device = scannedDevice,
                        disconnectEnabled = disconnectEnabled,
                        onDisconnect = scanState.bleConnectEvents.onDisconnect,
                        services = services,
                        onControlClick = onControlClick,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                DeviceDetails(scannedDevice)
            }
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

@ThemePreviews
@Composable
fun PreviewDeviceDetailScreen(
    @PreviewParameter(PortraitPreviewParams::class) featureParams: FeatureParams
) {
    BLEScannerTheme() {
        Column {
            ShowDeviceDetail(
                scanState = featureParams.scanState,
                onControlClick = {},
                appLayoutInfo = featureParams.appLayoutInfo,
                onBackClicked = {}
            )
        }
    }
}

@LandscapeThemePreviews
@Composable
fun PreviewLandscapeDeviceDetailScreen(
    @PreviewParameter(LandscapePreviewParams::class) featureParams: FeatureParams
) {
    BLEScannerTheme() {
        Column {
            ShowDeviceDetail(
                scanState = featureParams.scanState,
                onControlClick = {},
                appLayoutInfo = featureParams.appLayoutInfo,
                onBackClicked = {}
            )
        }
    }
}

