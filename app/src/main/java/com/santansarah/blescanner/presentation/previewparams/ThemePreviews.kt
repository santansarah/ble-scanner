package com.santansarah.blescanner.presentation.previewparams

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF17191b,
    group = "theme mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df,
    group = "theme mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df,
    group = "theme mode", device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait"
)
annotation class ThemePreviews

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df,
    group = "theme mode",
    device = "spec:width=411.4dp,height=914.3dp,orientation=landscape",
    apiLevel = 31
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF17191b,
    group = "theme mode",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    apiLevel = 31
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df,
    group = "theme mode",
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait",
    apiLevel = 31
)
annotation class LandscapeThemePreviews
