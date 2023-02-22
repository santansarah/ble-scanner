package com.santansarah.blescanner.presentation.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.domain.models.ScanFilterOption
import com.santansarah.blescanner.presentation.previewparams.FeatureParams
import com.santansarah.blescanner.presentation.previewparams.LandscapePreviewParams
import com.santansarah.blescanner.presentation.previewparams.LandscapeThemePreviews
import com.santansarah.blescanner.presentation.previewparams.PortraitPreviewParams
import com.santansarah.blescanner.presentation.previewparams.ThemePreviews
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo

@Composable
fun DeviceListScreen(
    paddingValues: PaddingValues,
    devices: List<ScannedDevice>,
    onClick: (String) -> Unit,
    onFilter: (ScanFilterOption?) -> Unit,
    scanFilterOption: ScanFilterOption?,
    onFavorite: (ScannedDevice) -> Unit,
    onForget: (ScannedDevice) -> Unit,
    appLayoutInfo: AppLayoutInfo
) {
    Column(
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding()
        )
    ) {

        if (appLayoutInfo.appLayoutMode.isLandscape()) {
            Row() {
                    ScanFilters(
                        onFilter = onFilter,
                        scanFilterOption = scanFilterOption,
                        appLayoutInfo = appLayoutInfo
                    )
                ScannedDeviceList(devices, onClick, onFavorite, onForget)
            }
        } else {

            ScanFilters(
                onFilter = onFilter,
                scanFilterOption = scanFilterOption,
                appLayoutInfo = appLayoutInfo
            )
            ScannedDeviceList(devices, onClick, onFavorite, onForget)
        }

    }
}

@Composable
fun ScannedDeviceList(
    devices: List<ScannedDevice>,
    onClick: (String) -> Unit,
    onFavorite: (ScannedDevice) -> Unit,
    onForget: (ScannedDevice) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(devices) { device ->

            ScannedDevice(
                device = device, onClick = onClick, onFavorite = onFavorite,
                onForget = onForget
            )
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}


@ThemePreviews
@Composable
fun PreviewDeviceListScreen(
    @PreviewParameter(PortraitPreviewParams::class) featureParams: FeatureParams
) {
    BLEScannerTheme() {
        Column {
            DeviceListScreen(
                paddingValues = PaddingValues(),
                devices = featureParams.scannedDevice,
                onClick = {},
                onFilter = {},
                scanFilterOption = ScanFilterOption.FAVORITES,
                onFavorite = {},
                onForget = {},
                appLayoutInfo = featureParams.appLayoutInfo
            )
        }
    }
}

@LandscapeThemePreviews
@Composable
fun PreviewLandscapeDeviceListScreen(
    @PreviewParameter(LandscapePreviewParams::class) featureParams: FeatureParams
) {
    BLEScannerTheme() {
        Column {
            DeviceListScreen(
                paddingValues = PaddingValues(),
                devices = featureParams.scannedDevice,
                onClick = {},
                onFilter = {},
                scanFilterOption = ScanFilterOption.FAVORITES,
                onFavorite = {},
                onForget = {},
                appLayoutInfo = featureParams.appLayoutInfo
            )
        }
    }
}