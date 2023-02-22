package com.santansarah.blescanner.presentation.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.domain.models.ScanState
import com.santansarah.blescanner.presentation.previewparams.FeatureParams
import com.santansarah.blescanner.presentation.previewparams.LandscapePreviewParams
import com.santansarah.blescanner.presentation.previewparams.LandscapeThemePreviews
import com.santansarah.blescanner.presentation.previewparams.PortraitPreviewParams
import com.santansarah.blescanner.presentation.previewparams.ThemePreviews
import com.santansarah.blescanner.presentation.scan.device.ShowDeviceBody
import com.santansarah.blescanner.presentation.scan.device.ShowDeviceDetail
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo

@Composable
fun HomeScreen(
    appLayoutInfo: AppLayoutInfo,
    scanState: ScanState,
    onControlClick: (String) -> Unit,
    onShowUserMessage: (String) -> Unit,
    onSave: (String) -> Unit,
    isEditing: Boolean,
    onEdit: (Boolean) -> Unit
) {

    if (appLayoutInfo.appLayoutMode.isLandscape()) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ShowDeviceDetail(
                scanUi = scanState.scanUI,
                bleConnectEvents = scanState.bleConnectEvents,
                onControlClick = onControlClick,
                appLayoutInfo = appLayoutInfo
            )
            ShowDeviceBody(
                appLayoutInfo = appLayoutInfo,
                scanUi = scanState.scanUI,
                bleReadWriteCommands = scanState.bleReadWriteCommands,
                onShowUserMessage = onShowUserMessage,
                onEdit = onEdit,
                isEditing = isEditing,
                onSave = onSave
            )
        }
    } else {
        ShowDeviceDetail(
            scanUi = scanState.scanUI,
            bleConnectEvents = scanState.bleConnectEvents,
            onControlClick = onControlClick,
            appLayoutInfo = appLayoutInfo
        )
        Spacer(modifier = Modifier.height(24.dp))

        ShowDeviceBody(
            appLayoutInfo = appLayoutInfo,
            scanUi = scanState.scanUI,
            bleReadWriteCommands = scanState.bleReadWriteCommands,
            onShowUserMessage = onShowUserMessage,
            onEdit =onEdit,
            isEditing = isEditing,
            onSave = onSave
        )
    }
}

@ThemePreviews
@Composable
fun PreviewHomeScreen(
    @PreviewParameter(PortraitPreviewParams::class) featureParams: FeatureParams
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
                onEdit = {}
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
            HomeScreen(
                appLayoutInfo = featureParams.appLayoutInfo,
                scanState = featureParams.scanState,
                onControlClick = {},
                onShowUserMessage = {},
                onSave = {},
                isEditing = false,
                onEdit = {}
            )
        }
    }
}

