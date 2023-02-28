package com.santansarah.blescanner.presentation.scan.device

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.domain.models.DeviceDetail
import com.santansarah.blescanner.domain.models.DeviceService
import com.santansarah.blescanner.domain.models.propsToString
import com.santansarah.blescanner.presentation.previewparams.PreviewDeviceDetailProvider
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.presentation.theme.bodySmallItalic
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo
import com.santansarah.blescanner.utils.windowinfo.AppLayoutMode

@Composable
fun ServicePager(
    appLayoutInfo: AppLayoutInfo,
    selectedDevice: DeviceDetail,
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit,
    onWrite: (String, String) -> Unit,
    onReadDescriptor: (String, String) -> Unit,
    onWriteDescriptor: (String, String, String) -> Unit
) {

    val mainBodyModifier = when(appLayoutInfo.appLayoutMode) {
        AppLayoutMode.LANDSCAPE_NORMAL -> Modifier.padding(horizontal = 20.dp)
        AppLayoutMode.LANDSCAPE_BIG -> Modifier.padding(horizontal = 40.dp)
        AppLayoutMode.PORTRAIT_NARROW -> Modifier.padding(horizontal = 16.dp)
        else -> Modifier
    }

    if (selectedDevice.services.isNotEmpty()) {
        Column(
            modifier = mainBodyModifier
        ) {
            val services = selectedDevice.services
            val totalServices by rememberSaveable { mutableStateOf(services.count()) }
            var currentServiceIdx by rememberSaveable { mutableStateOf(0) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                //.background(Color.White.copy(.3f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(.3f)
                    ),
                    enabled = (currentServiceIdx > 0),
                    onClick = {
                        currentServiceIdx--
                    }
                ) {
                    Icon(
                        //modifier = Modifier.align(Alignment.Top),
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Next Service",
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = services[currentServiceIdx].name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(.3f)
                    ),
                    enabled = (currentServiceIdx != (totalServices - 1)),
                    onClick = {
                        currentServiceIdx++
                    }
                ) {
                    Icon(
                        //modifier = Modifier.align(Alignment.Top),
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next Service",
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                text = services[currentServiceIdx].uuid,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            ServicePagerDetail(
                services[currentServiceIdx],
                onRead,
                onShowUserMessage,
                onWrite,
                onReadDescriptor,
                onWriteDescriptor
            )
        }
    }
}


@Composable
fun ServicePagerDetail(
    service: DeviceService,
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit,
    onWrite: (String, String) -> Unit,
    onReadDescriptor: (String, String) -> Unit,
    onWriteDescriptor: (String, String, String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {

        service.characteristics.forEach { char ->
            OutlinedCard(
                modifier = Modifier
                    .defaultMinSize(minHeight = 200.dp)
            ) {
                var state by rememberSaveable { mutableStateOf(0) }
                var expanded by rememberSaveable { mutableStateOf(false) }

                Column(
                    modifier = Modifier.padding(6.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(.85f)
                        ) {

                            Text(
                                text = char.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = char.uuid.uppercase(),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = char.properties.propsToString(),
                                style = bodySmallItalic
                            )
                        }

                        ReadWriteMenu(
                            expanded = expanded,
                            onExpanded = { expanded = it },
                            onState = { state = it })
                    }

                    Spacer(modifier = Modifier.height(10.dp))


                    if (state == 0) {
                        ReadCharacteristic(char, onRead, onShowUserMessage)
                    } else {
                        WriteCharacteristic(char, onWrite)
                    }

                    if (char.descriptors.isNotEmpty()) {

                        char.descriptors.forEach { desc ->

                            var descriptorState by rememberSaveable { mutableStateOf(0) }
                            var descriptorExpanded by rememberSaveable { mutableStateOf(false) }

                            Divider(modifier = Modifier.padding(vertical = 10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column(
                                    modifier = Modifier.fillMaxWidth(.85f)
                                ) {

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            modifier = Modifier.size(22.dp),
                                            painter = painterResource(id = R.drawable.descriptor),
                                            contentDescription = "Descriptor Icon",
                                            tint = MaterialTheme.colorScheme.onSecondary
                                        )
                                        Text(
                                            modifier = Modifier.padding(start = 4.dp),
                                            text = desc.name,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                    Text(
                                        //modifier = Modifier.padding(start = 12.dp),
                                        text = desc.uuid.uppercase(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }

                                ReadWriteMenu(
                                    expanded = descriptorExpanded,
                                    onExpanded = { descriptorExpanded = it },
                                    onState = { descriptorState = it })
                            }

                            if (descriptorState == 0) {
                                ReadDescriptor(
                                    char.uuid,
                                    descriptor = desc, onRead = onReadDescriptor,
                                    onShowUserMessage = onShowUserMessage
                                )
                            } else
                                WriteDescriptor(descriptor = desc, onWrite = onWriteDescriptor)

                        }
                        //}
                    }

                }
            }
            if (service.characteristics.indexOf(char) < service.characteristics.count() - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }


            /*char.descriptors.forEach { desc ->
                    Text(text = "- ${desc.uuid}")
                    Text(text = desc.permissions.toString())
                }*/
        }


    }

}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF17191b
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc0e5dd
)
@Composable
fun PreviewServicePager(
    @PreviewParameter(PreviewDeviceDetailProvider::class) deviceDetail: DeviceDetail
) {
    BLEScannerTheme {

        Column {
            ServicePager(
                appLayoutInfo = AppLayoutInfo(
                    appLayoutMode = AppLayoutMode.PORTRAIT,
                    windowDpSize = DpSize(392.dp, 850.dp),
                    foldableInfo = null
                ),
                deviceDetail,
                {},
                {},
                { _: String, _: String -> },
                { _: String, _: String -> },
                { _: String, _: String, _: String -> },
            )
        }
    }
}
