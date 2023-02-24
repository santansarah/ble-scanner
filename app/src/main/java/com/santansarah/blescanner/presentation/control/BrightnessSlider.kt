package com.santansarah.blescanner.presentation.control

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo

@Composable
fun AdjustBrightness(
    appLayoutInfo: AppLayoutInfo,
    onBrightnessChanged: (Int) -> Unit
) {

    Spacer(modifier = Modifier.height(10.dp))

    var sliderPosition by rememberSaveable { mutableStateOf(100f) }

    if (!appLayoutInfo.appLayoutMode.isLandscape()) {
        OutlinedCard(
            modifier = Modifier.padding(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            BrightnessSlider(
                sliderPosition = sliderPosition,
                onPositionValue = { sliderPosition = it },
                onBrightnessChanged = onBrightnessChanged
            )

        }
    } else
        BrightnessSlider(
            sliderPosition = sliderPosition,
            onPositionValue = { sliderPosition = it },
            onBrightnessChanged = onBrightnessChanged
        )

}

@Composable
private fun BrightnessSlider(
    sliderPosition: Float,
    onPositionValue: (Float) -> Unit,
    onBrightnessChanged: (Int) -> Unit
) {

    Slider(
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        valueRange = 0f..100f,
        value = sliderPosition,
        onValueChange = {
            onPositionValue(it)
            onBrightnessChanged(it.toInt())
        })
}