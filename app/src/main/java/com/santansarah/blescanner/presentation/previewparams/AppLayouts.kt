package com.santansarah.blescanner.presentation.previewparams

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo
import com.santansarah.blescanner.utils.windowinfo.AppLayoutMode


val portrait = AppLayoutInfo(
    appLayoutMode = AppLayoutMode.PORTRAIT,
    windowDpSize = DpSize(0.dp, 0.dp),
    foldableInfo = null
)

val portraitNarrow = AppLayoutInfo(
    appLayoutMode = AppLayoutMode.PORTRAIT_NARROW,
    windowDpSize = DpSize(0.dp, 0.dp),
    foldableInfo = null
)

val landscapeNormal = AppLayoutInfo(
    appLayoutMode = AppLayoutMode.LANDSCAPE_NORMAL,
    windowDpSize = DpSize(0.dp, 0.dp),
    foldableInfo = null
)

val landscapeBig = AppLayoutInfo(
    appLayoutMode = AppLayoutMode.LANDSCAPE_BIG,
    windowDpSize = DpSize(0.dp, 0.dp),
    foldableInfo = null
)