package com.santansarah.blescanner.utils.windowinfo

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.window.layout.FoldingFeature
import timber.log.Timber

enum class AppLayoutMode {
    LANDSCAPE_NORMAL,
    LANDSCAPE_BIG,
    PORTRAIT;

    fun isLandscape(): Boolean = (this == LANDSCAPE_BIG || this ==LANDSCAPE_NORMAL)

}

@Composable
fun getWindowLayoutType(
    windowInfo: WindowClassWithSize,
    foldableInfo: FoldableInfo?

): AppLayoutInfo = with(windowInfo) {
    Timber.tag("debug")
        .d("windowWH: " + windowWidth + ";" + windowHeight + ", " + size.width + ";" + size.height)
    Timber.tag("debug").d("screenInfo: " + windowInfo.rotation + ";" + foldableInfo?.bounds)

    // First, I check to see if it's a foldable, with dual screen (isSeparating).
    if ((foldableInfo != null) && foldableInfo.showSeparateScreens) {
        getFoldableAppLayout(foldableInfo, windowInfo.size)
    } else {
        // Check for a typical phone size, landscape mode.
        if (windowHeight == WindowHeightSizeClass.Compact)
            getLandscapeLayout(windowWidth, windowInfo.size)
        else {
            getPortraitLayout(windowWidth, windowInfo.size)
        }
    }
}

@Composable
fun getFoldableAppLayout(foldableInfo: FoldableInfo, size: DpSize): AppLayoutInfo {
    return if (foldableInfo.orientation == FoldingFeature.Orientation.VERTICAL)
        AppLayoutInfo(
            AppLayoutMode.PORTRAIT,
            size,
            foldableInfo
        )
    else
        AppLayoutInfo(
            AppLayoutMode.LANDSCAPE_BIG,
            size,
            foldableInfo
        )
}

fun getLandscapeLayout(windowWidth: WindowWidthSizeClass, size: DpSize): AppLayoutInfo =
    AppLayoutInfo(AppLayoutMode.LANDSCAPE_NORMAL, size)


fun getPortraitLayout(windowWidth: WindowWidthSizeClass, size: DpSize): AppLayoutInfo =
// At this point, I know it's not a landscape/rotated phone size.
    // So let's check the width.
    when (windowWidth) {
        WindowWidthSizeClass.Compact -> AppLayoutInfo(AppLayoutMode.PORTRAIT, size)
        WindowWidthSizeClass.Medium -> AppLayoutInfo(AppLayoutMode.LANDSCAPE_NORMAL, size)
        else -> AppLayoutInfo(AppLayoutMode.LANDSCAPE_BIG, size)
    }


