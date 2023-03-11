package com.santansarah.scan.utils.windowinfo

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.DpSize

data class AppLayoutInfo(
    val appLayoutMode: AppLayoutMode,
    val windowDpSize: DpSize,
    val foldableInfo: FoldableInfo? = null
)

enum class CurrentRotation() {
    ROTATION_0,
    ROTATION_90,
    ROTATION_180,
    ROTATION_270
}

data class WindowClassWithSize(
    val windowWidth: WindowWidthSizeClass,
    val windowHeight: WindowHeightSizeClass,
    val size: DpSize,
    val rotation: CurrentRotation = CurrentRotation.ROTATION_0,
    //val bounds: Rect
)
