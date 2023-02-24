package com.santansarah.blescanner.presentation.control

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorWheel(
    onColorChanged: (String) -> Unit
) {
    val controller = rememberColorPickerController()

    HsvColorPicker(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .padding(6.dp),
        controller = controller,
        onColorChanged = { colorEnvelope: ColorEnvelope ->
            val hexCode: String =
                colorEnvelope.hexCode // Color hex code, which represents color value.
            onColorChanged(hexCode.substringAfter("FF"))
        }
    )
}