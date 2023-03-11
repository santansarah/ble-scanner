package com.santansarah.scan.utils.windowinfo

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.view.Surface
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.window.layout.WindowMetricsCalculator


@ExperimentalMaterial3WindowSizeClassApi
@Composable
fun getWindowSizeClasses(activity: ComponentActivity): WindowClassWithSize {

    val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
    val size = getWindowDpSizeFromRect(metrics.bounds)

    //val bounds = activity.windowManager.currentWindowMetrics.bounds

    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    return WindowClassWithSize(
        windowWidth = windowSizeClass.widthSizeClass,
        windowHeight = windowSizeClass.heightSizeClass,
        size = size,
        rotation = getRotation(activity)
        // API 30+ CurrentRotation.values()[activity.display?.rotation ?: 0],
        //bounds = bounds
    )



}

fun getRotation(context: ComponentActivity): CurrentRotation {
    return when ((context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        .defaultDisplay.orientation) {
        Surface.ROTATION_0 -> CurrentRotation.ROTATION_0
        Surface.ROTATION_90 -> CurrentRotation.ROTATION_90
        Surface.ROTATION_180 -> CurrentRotation.ROTATION_180
        Surface.ROTATION_270 -> CurrentRotation.ROTATION_270
        else -> CurrentRotation.ROTATION_0
    }
}


@Composable
fun getWindowDpSizeFromRect(rect: Rect): DpSize {
    LocalConfiguration.current
    val density = LocalDensity.current
    return with(density) { rect.toComposeRect().size.toDpSize() }
}

@Composable
fun Rect.toDpRect(): RectF {
    LocalConfiguration.current
    val density = LocalDensity.current

    val dpRect = with(density) {
        RectF(this@toDpRect.left.toDp().value,
            this@toDpRect.top.toDp().value,
            this@toDpRect.right.toDp().value,
            this@toDpRect.bottom.toDp().value)
    }

    return dpRect
}
