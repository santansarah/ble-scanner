package com.santansarah.blescanner.presentation.control

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.santansarah.blescanner.R
import com.santansarah.blescanner.domain.bleparsables.ELKBLEDOM
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.presentation.components.AppBarWithBackButton
import com.santansarah.blescanner.presentation.scan.device.ConnectButtons
import com.santansarah.blescanner.presentation.scan.device.DeviceButtons
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreen(
    vm: ControlViewModel = koinViewModel(),
    onBackClicked: () -> Unit
) {

    val controlState = vm.controlState.collectAsStateWithLifecycle().value

    val appSnackBarHostState = remember { SnackbarHostState() }
    controlState.userMessage?.let { userMessage ->
        LaunchedEffect(controlState.userMessage, userMessage) {
            appSnackBarHostState.showSnackbar(userMessage)
            vm.userMessageShown()
        }
    }

    if (controlState.device != null) {
        Scaffold(
            // modifier = Modifier.border(2.dp, Color.Magenta),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = appSnackBarHostState) },
            topBar = {
                AppBarWithBackButton(
                    onBackClicked = onBackClicked,
                    device = controlState.device,
                    onEdit = { },
                    onFavorite = {},
                    onForget = {}
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                //.padding(horizontal = 8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                    //.background(MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val connectEnabled =
                                !(controlState.bleMessage == ConnectionState.CONNECTING ||
                                        controlState.bleMessage == ConnectionState.CONNECTED)
                            val disconnectEnabled =
                                !(controlState.bleMessage == ConnectionState.DISCONNECTING ||
                                        controlState.bleMessage == ConnectionState.DISCONNECTED)

                            Row() {
                                ConnectButtons(
                                    connectEnabled, vm::onConnect,
                                    controlState.device, disconnectEnabled, vm::onDisconnect
                                )
                            }

                            var checked by remember { mutableStateOf(false) }

                            FilledIconToggleButton(
                                colors = IconButtonDefaults.iconToggleButtonColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
                                    contentColor = MaterialTheme.colorScheme.outline,
                                    checkedContainerColor = MaterialTheme.colorScheme.primary,
                                    checkedContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                checked = vm.getOnOffState(),
                                onCheckedChange = { vm.toggleOnOff() }) {
                                if (vm.getOnOffState())
                                    Icon(
                                        painter = painterResource(id = R.drawable.light_on),
                                        contentDescription = "On/Off"
                                    )
                                else
                                    Icon(
                                        painter = painterResource(id = R.drawable.light_off),
                                        contentDescription = "On/Off"
                                    )
                            }

                        }
                    }
                }

                ShowColors(vm::changeColor, vm::changeBrightness)
            }
        }
    }
}

@Composable
fun ShowColors(
    onColorChanged: (String) -> Unit,
    onBrightnessChanged: (Int) -> Unit
) {

    Spacer(modifier = Modifier.height(10.dp))

    var selectedColorIdx by rememberSaveable {
        mutableStateOf(-1)
    }

    LazyVerticalGrid(
        // modifier = Modifier.padding(4.dp),
        columns = GridCells.Fixed(4)
    ) {
        itemsIndexed(ELKBLEDOM.colorsHex) { idx, color ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(6.dp)
                    .clickable {
                        selectedColorIdx = idx
                        onColorChanged(color)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color("#$color".toColorInt()))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(6.dp))
                )
            }
        }
    }

    val controller = rememberColorPickerController()

    HsvColorPicker(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(10.dp),
        controller = controller,
        onColorChanged = { colorEnvelope: ColorEnvelope ->
            val hexCode: String =
                colorEnvelope.hexCode // Color hex code, which represents color value.
            onColorChanged(hexCode.substringAfter("FF"))
        }
    )

    var sliderPosition by rememberSaveable { mutableStateOf(100f) }
    Slider(
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.secondary,
            activeTrackColor = MaterialTheme.colorScheme.secondary,
            inactiveTrackColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = Modifier.fillMaxWidth(),
        valueRange = 0f..100f,
        value = sliderPosition,
        onValueChange = {
        sliderPosition = it
        onBrightnessChanged(it.toInt())
    })


}