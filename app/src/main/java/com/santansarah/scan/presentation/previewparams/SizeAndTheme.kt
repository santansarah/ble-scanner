package com.santansarah.scan.presentation.previewparams

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


// phone; portrait
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df, device = "spec:width=411.4dp,height=914.3dp"
)
annotation class PortraitLayouts  // width = 600 dp and below

// narrow tablet; portrait
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df, device = "spec:width=601dp,height=960dp,dpi=320"
)
annotation class PortraitNarrowLayouts  // width = 650 dp and below

//phone; landscape normal
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF17191b, device = "spec:width=411.4dp,height=914.3dp,orientation=landscape"
)
// large tablet; landscape normal (tablet in portrait mode)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF17191b,
    device = "spec:width=1280dp,height=900dp,dpi=320,orientation=portrait"
)
annotation class LandscapeLayouts // width = 651..950

// large tablet; landscape big
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF17191b, device = "spec:width=1280dp,height=900dp,dpi=320"
)
annotation class LandscapeBig



@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df,
    group = "portrait"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df,
    group = "portrait"
)






@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF17191b,
    group = "portrait"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc6e8df,
    group = "portrait"
)
annotation class PortraitPreviews

/**
 * If the width is greater than 600, I'm setting my `AppLayoutMode` to Landscape.
 */
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
annotation class LandscapePreviews
