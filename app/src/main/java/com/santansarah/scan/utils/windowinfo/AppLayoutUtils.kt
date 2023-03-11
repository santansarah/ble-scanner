package com.santansarah.scan.utils.windowinfo

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.FoldingFeature
import timber.log.Timber

enum class AppLayoutMode {
    LANDSCAPE_NORMAL,
    LANDSCAPE_BIG,
    PORTRAIT,
    PORTRAIT_NARROW;

    fun isLandscape(): Boolean = (this == LANDSCAPE_BIG || this == LANDSCAPE_NORMAL)

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
            getPhoneLandscape(windowWidth, windowInfo.size)
        else {
            getLayoutType(windowWidth, windowInfo.size)
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

fun getPhoneLandscape(windowWidth: WindowWidthSizeClass, size: DpSize): AppLayoutInfo =
    AppLayoutInfo(AppLayoutMode.LANDSCAPE_NORMAL, size)


fun getLayoutType(windowWidth: WindowWidthSizeClass, size: DpSize): AppLayoutInfo =
// At this point, I know it's not a landscape/rotated phone size.
    // So let's check the width.
    when (windowWidth) {
        WindowWidthSizeClass.Compact -> AppLayoutInfo(AppLayoutMode.PORTRAIT, size)
        WindowWidthSizeClass.Medium -> {
            // some tablets measure 600.93896; just over 600;
            // let's give this some padding, and make a new cut-off.
            if (size.width <= 650.dp)
                AppLayoutInfo(
                    AppLayoutMode.PORTRAIT_NARROW,
                    size
                )
            else
                AppLayoutInfo(
                    AppLayoutMode.LANDSCAPE_NORMAL,
                    size
                )
        }
        else -> {
            // override the expanded threshold. 800 vs 1000+ is big diff.
            if (size.width < 950.dp)
                AppLayoutInfo(
                    AppLayoutMode.LANDSCAPE_NORMAL,
                    size
                )
            else
                AppLayoutInfo(AppLayoutMode.LANDSCAPE_BIG, size)
        }
    }


