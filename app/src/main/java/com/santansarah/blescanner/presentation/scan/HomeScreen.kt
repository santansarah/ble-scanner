package com.santansarah.blescanner.presentation.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.domain.models.DeviceEvents
import com.santansarah.blescanner.domain.models.ScanState
import com.santansarah.blescanner.presentation.previewparams.DeviceBigParams
import com.santansarah.blescanner.presentation.previewparams.DeviceLandscapeParams
import com.santansarah.blescanner.presentation.previewparams.DevicePortraitParams
import com.santansarah.blescanner.presentation.previewparams.FeatureParams
import com.santansarah.blescanner.presentation.previewparams.LandscapeBig
import com.santansarah.blescanner.presentation.previewparams.LandscapeLayouts
import com.santansarah.blescanner.presentation.previewparams.PortraitLayouts
import com.santansarah.blescanner.presentation.scan.device.ShowDeviceBody
import com.santansarah.blescanner.presentation.scan.device.ShowDeviceDetail
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo

@Composable
fun HomeScreen(
    appLayoutInfo: AppLayoutInfo,
    scanState: ScanState,
    onControlClick: (String) -> Unit,
    deviceEvents: DeviceEvents,
    isEditing: Boolean,
    onBackClicked: () -> Unit,
    onSave: (String) -> Unit,
    onShowUserMessage: (String) -> Unit
) {

    if (appLayoutInfo.appLayoutMode.isLandscape()) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {

            val sidePanelModifier = Modifier.weight(2f)

            Column(
                modifier = sidePanelModifier
            ) {
                ShowDeviceDetail(
                    scanState = scanState,
                    onBackClicked = onBackClicked,
                    onControlClick = onControlClick,
                    appLayoutInfo = appLayoutInfo
                )
            }
            Column(
                modifier = Modifier.weight(3f)
            ) {
                ShowDeviceBody(
                    appLayoutInfo = appLayoutInfo,
                    scanUi = scanState.scanUI,
                    bleReadWriteCommands = scanState.bleReadWriteCommands,
                    onShowUserMessage = onShowUserMessage,
                    onEdit = deviceEvents.onIsEditing,
                    isEditing = isEditing,
                    onSave = onSave
                )
            }
        }
    } else {
        ShowDeviceDetail(
            scanState = scanState,
            onBackClicked = onBackClicked,
            onControlClick = onControlClick,
            appLayoutInfo = appLayoutInfo
        )
        Spacer(modifier = Modifier.height(24.dp))

        ShowDeviceBody(
            appLayoutInfo = appLayoutInfo,
            scanUi = scanState.scanUI,
            bleReadWriteCommands = scanState.bleReadWriteCommands,
            onShowUserMessage = onShowUserMessage,
            onEdit = deviceEvents.onIsEditing,
            isEditing = isEditing,
            onSave = onSave
        )
    }
}


@PortraitLayouts
@Composable
fun PreviewHomeScreen(
    @PreviewParameter(DevicePortraitParams::class) featureParams: FeatureParams
) {
    BLEScannerTheme() {
        Column {
            HomeScreen(
                appLayoutInfo = featureParams.appLayoutInfo,
                scanState = featureParams.scanState,
                onControlClick = {},
                onShowUserMessage = {},
                onSave = {},
                isEditing = false,
                onBackClicked = {},
                deviceEvents = DeviceEvents({},{},{},{},{})
            )
        }
    }
}



@LandscapeLayouts
@Composable
fun PreviewLandscapeDeviceDetailScreen(
    @PreviewParameter(DeviceLandscapeParams::class) featureParams: FeatureParams
) {
    BLEScannerTheme() {
        Column {
            HomeScreen(
                appLayoutInfo = featureParams.appLayoutInfo,
                scanState = featureParams.scanState,
                onControlClick = {},
                onShowUserMessage = {},
                onSave = {},
                isEditing = false,
                onBackClicked = {},
                deviceEvents = DeviceEvents({},{},{},{},{})
            )
        }
    }
}

@LandscapeBig
@Composable
fun PreviewLandscapeBigDeviceDetailScreen(
    @PreviewParameter(DeviceBigParams::class) featureParams: FeatureParams
) {
    BLEScannerTheme() {
        Column {
            HomeScreen(
                appLayoutInfo = featureParams.appLayoutInfo,
                scanState = featureParams.scanState,
                onControlClick = {},
                onShowUserMessage = {},
                onSave = {},
                isEditing = false,
                onBackClicked = {},
                deviceEvents = DeviceEvents({},{},{},{},{})
            )
        }
    }
}
