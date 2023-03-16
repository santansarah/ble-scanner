package com.santansarah.scan.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.santansarah.scan.R
import com.santansarah.scan.presentation.previewparams.FeatureParams
import com.santansarah.scan.presentation.previewparams.LandscapeBig
import com.santansarah.scan.presentation.previewparams.LandscapeBigListParams
import com.santansarah.scan.presentation.previewparams.LandscapeLayouts
import com.santansarah.scan.presentation.previewparams.LandscapeListParams
import com.santansarah.scan.presentation.previewparams.PortraitLayouts
import com.santansarah.scan.presentation.previewparams.PortraitListParams
import com.santansarah.scan.presentation.previewparams.PortraitNarrowLayouts
import com.santansarah.scan.presentation.previewparams.PortraitNarrowListParams
import com.santansarah.scan.presentation.theme.SanTanScanTheme
import com.santansarah.scan.presentation.theme.pagerHeaders
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo
import com.santansarah.scan.utils.windowinfo.AppLayoutMode

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissions(
    appLayoutInfo: AppLayoutInfo,
    paddingValues: PaddingValues,
    multiplePermissionsState: MultiplePermissionsState,
    onAboutClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = paddingValues.calculateTopPadding()
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val cardPadding = when(appLayoutInfo.appLayoutMode) {
            AppLayoutMode.LANDSCAPE_BIG -> PaddingValues(150.dp)
            AppLayoutMode.LANDSCAPE_NORMAL -> PaddingValues(horizontal = 60.dp)
            AppLayoutMode.PORTRAIT -> PaddingValues(16.dp)
            AppLayoutMode.PORTRAIT_NARROW -> PaddingValues(40.dp)
        }

        OutlinedCard(
            modifier = Modifier.padding(cardPadding)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(2f)
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.get_started),
                            contentDescription = "Get Started Icon",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                        Text(
                            modifier = Modifier.padding(start = 6.dp),
                            text = "Get Started",
                            style = pagerHeaders,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .offset(y = (-1).dp),
                        painter = painterResource(id = R.drawable.unlock),
                        contentDescription = "Unlock Icon",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Divider(modifier = Modifier.padding(top = 8.dp))
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Before you can start scanning for BLE devices, first, " +
                            "you'll need to enable some Bluetooth and Location permissions.",

                    )

                Spacer(modifier = Modifier.height(30.dp))

                if (appLayoutInfo.appLayoutMode.isLandscape()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(.4f, false)
                        ) {
                            AllowButton { multiplePermissionsState.launchMultiplePermissionRequest() }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(.4f, false)
                        ) {
                            AboutButton(onAboutClick)
                        }
                    }
                } else {
                    AllowButton { multiplePermissionsState.launchMultiplePermissionRequest() }
                    Spacer(modifier = Modifier.height(10.dp))
                    AboutButton(onAboutClick)
                }
            }
        }
    }

}

@Composable
private fun AboutButton(onAboutClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        onClick = {
            onAboutClick()
        }) {
        Text(text = "Help & Privacy")
    }
}

@Composable
private fun AllowButton(
    onAllowClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        onClick = {
            onAllowClick()
        }) {
        Text(text = "Allow Permissions")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@PortraitLayouts
@Composable
fun PermissionsPreview(
    @PreviewParameter(PortraitListParams::class) featureParams: FeatureParams
) {
    SanTanScanTheme(dynamicColor = false) {
        Surface() {
            ShowPermissions(
                featureParams.appLayoutInfo,
                paddingValues = PaddingValues(0.dp),
                multiplePermissionsState = object : MultiplePermissionsState {
                    override val allPermissionsGranted: Boolean
                        get() = TODO("Not yet implemented")
                    override val permissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val revokedPermissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val shouldShowRationale: Boolean
                        get() = TODO("Not yet implemented")

                    override fun launchMultiplePermissionRequest() {
                        TODO("Not yet implemented")
                    }
                },
                {})
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@PortraitNarrowLayouts
@Composable
fun PermissionsNarrowPreview(
    @PreviewParameter(PortraitNarrowListParams::class) featureParams: FeatureParams
) {
    SanTanScanTheme(dynamicColor = false) {
        Surface() {
            ShowPermissions(
                featureParams.appLayoutInfo,
                paddingValues = PaddingValues(0.dp),
                multiplePermissionsState = object : MultiplePermissionsState {
                    override val allPermissionsGranted: Boolean
                        get() = TODO("Not yet implemented")
                    override val permissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val revokedPermissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val shouldShowRationale: Boolean
                        get() = TODO("Not yet implemented")

                    override fun launchMultiplePermissionRequest() {
                        TODO("Not yet implemented")
                    }
                },
                {})
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@LandscapeLayouts
@Composable
fun PermissionsLandscapePreview(
    @PreviewParameter(LandscapeListParams::class) featureParams: FeatureParams
) {
    SanTanScanTheme(dynamicColor = false) {
        Surface() {
            ShowPermissions(
                featureParams.appLayoutInfo,
                paddingValues = PaddingValues(0.dp),
                multiplePermissionsState = object : MultiplePermissionsState {
                    override val allPermissionsGranted: Boolean
                        get() = TODO("Not yet implemented")
                    override val permissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val revokedPermissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val shouldShowRationale: Boolean
                        get() = TODO("Not yet implemented")

                    override fun launchMultiplePermissionRequest() {
                        TODO("Not yet implemented")
                    }
                },
                {})
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@LandscapeBig
@Composable
fun PermissionsLandscapeBigPreview(
    @PreviewParameter(LandscapeBigListParams::class) featureParams: FeatureParams
) {
    SanTanScanTheme(dynamicColor = false) {
        Surface() {
            ShowPermissions(
                featureParams.appLayoutInfo,
                paddingValues = PaddingValues(0.dp),
                multiplePermissionsState = object : MultiplePermissionsState {
                    override val allPermissionsGranted: Boolean
                        get() = TODO("Not yet implemented")
                    override val permissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val revokedPermissions: List<PermissionState>
                        get() = TODO("Not yet implemented")
                    override val shouldShowRationale: Boolean
                        get() = TODO("Not yet implemented")

                    override fun launchMultiplePermissionRequest() {
                        TODO("Not yet implemented")
                    }
                },
                {})
        }
    }
}