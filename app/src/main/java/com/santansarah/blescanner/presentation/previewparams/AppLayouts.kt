package com.santansarah.blescanner.presentation.previewparams

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo
import com.santansarah.blescanner.utils.windowinfo.AppLayoutMode

class LayoutPreviews : PreviewParameterProvider<AppLayoutInfo> {
    override val values = sequenceOf(
        AppLayoutInfo(
            appLayoutMode = AppLayoutMode.LANDSCAPE_NORMAL,
            windowDpSize = DpSize(850.dp, 392.dp),
            foldableInfo = null
        ),
        AppLayoutInfo(
            appLayoutMode = AppLayoutMode.PORTRAIT,
            windowDpSize = DpSize(392.dp, 850.dp),
            foldableInfo = null
        )
    )
}