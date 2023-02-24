package com.santansarah.blescanner.presentation.control

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.blescanner.data.local.entities.displayName
import com.santansarah.blescanner.presentation.components.ControlAppBar
import com.santansarah.blescanner.presentation.scan.device.ConnectButtons
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreen(
    vm: ControlViewModel = koinViewModel(),
    appLayoutInfo: AppLayoutInfo,
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
                if (!appLayoutInfo.appLayoutMode.isLandscape()) {
                    ControlAppBar(appLayoutInfo = appLayoutInfo, onBackClicked = onBackClicked) {
                        Text(text = controlState.device.displayName())
                    }
                }
            }
        ) { padding ->

            val controlPadding = if (appLayoutInfo.appLayoutMode.isLandscape()) 0.dp
            else
                padding.calculateTopPadding()

            var selectedColorIdx by rememberSaveable {
                mutableStateOf(-1)
            }

            Column(
                modifier = Modifier
                    .padding(top = controlPadding)
                    .fillMaxSize()
            ) {

                val connectEnabled = !controlState.bleMessage.isActive()
                val disconnectEnabled = controlState.bleMessage.isActive()

                if (appLayoutInfo.appLayoutMode.isLandscape()) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                        ) {

                            ControlAppBar(
                                appLayoutInfo = appLayoutInfo,
                                onBackClicked = onBackClicked,
                                actionButtons = {
                                    Row() {
                                        ConnectButtons(
                                            connectEnabled, vm::onConnect,
                                            controlState.device, disconnectEnabled, vm::onDisconnect
                                        )
                                        OnOffButton(
                                            checked = vm.getOnOffState(),
                                            onCheckChanged = vm::toggleOnOff
                                        )
                                    }
                                }
                            )
                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = controlState.device.displayName(),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            StandardColorPicker(
                                appLayoutInfo = appLayoutInfo,
                                selectedColorIdx = selectedColorIdx,
                                onSelectedIdxChanged = { selectedColorIdx = it },
                                onColorChanged = vm::changeColor
                            )
                            AdjustBrightness(
                                appLayoutInfo = appLayoutInfo,
                                onBrightnessChanged = vm::changeBrightness
                            )
                        }
                        Column(modifier = Modifier.weight(3f)) {
                            ColorWheel(onColorChanged = vm::changeColor)
                        }

                    }
                } else {

                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row() {
                            ConnectButtons(
                                connectEnabled, vm::onConnect,
                                controlState.device, disconnectEnabled, vm::onDisconnect
                            )
                        }

                        OnOffButton(
                            checked = vm.getOnOffState(),
                            onCheckChanged = vm::toggleOnOff
                        )

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    StandardColorPicker(
                        appLayoutInfo = appLayoutInfo,
                        selectedColorIdx = selectedColorIdx,
                        onSelectedIdxChanged = { selectedColorIdx = it },
                        onColorChanged = vm::changeColor
                    )
                    ColorWheel(onColorChanged = vm::changeColor)
                    AdjustBrightness(
                        appLayoutInfo = appLayoutInfo,
                        onBrightnessChanged = vm::changeBrightness
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }
    }
}

