package com.santansarah.scan.utils.windowinfo

import android.graphics.Rect
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun getFoldableInfoFlow(activity: ComponentActivity) =
    WindowInfoTracker.getOrCreate(activity)
        .windowLayoutInfo(activity)
        .flowWithLifecycle(activity.lifecycle)
        .map { layoutInfo ->
            val foldingFeature =
                layoutInfo.displayFeatures
                    .filterIsInstance<FoldingFeature>()
                    .firstOrNull()

            Log.d("debug", "foldingFeature: $foldingFeature")
            Log.d("debug", "foldingFeature: ${foldingFeature?.isSeparating}")

            foldingFeature?.let {
                FoldableInfo(
                    hingeType = foldingFeature.occlusionType,
                    orientation = foldingFeature.orientation,
                    openedState = foldingFeature.state,
                    showSeparateScreens = foldingFeature.isSeparating,
                    bounds = foldingFeature.bounds
                )
            }
        }
        .stateIn(
            scope = activity.lifecycleScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

class FoldableInfo(
    val hingeType: FoldingFeature.OcclusionType,
    val orientation: FoldingFeature.Orientation,
    val openedState: FoldingFeature.State,
    val showSeparateScreens: Boolean,
    val bounds: Rect
) {

    fun isTableTopPosture() : Boolean {
        return this.openedState == FoldingFeature.State.HALF_OPENED &&
                this.orientation == FoldingFeature.Orientation.HORIZONTAL
    }

    fun isBookPosture() : Boolean {
        return this.openedState == FoldingFeature.State.HALF_OPENED &&
                this.orientation == FoldingFeature.Orientation.VERTICAL
    }

}
