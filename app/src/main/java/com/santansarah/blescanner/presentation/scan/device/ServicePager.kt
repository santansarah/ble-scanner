package com.santansarah.blescanner.presentation.scan.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.domain.models.DeviceDetail
import com.santansarah.blescanner.domain.models.DeviceService

@Composable
fun ServicePager(
    selectedDevice: DeviceDetail,
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit
) {
    if (selectedDevice.services.isNotEmpty()) {
        val services = selectedDevice.services
        val totalServices by rememberSaveable { mutableStateOf(services.count()) }
        var currentServiceIdx by rememberSaveable { mutableStateOf(0) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                enabled = (currentServiceIdx > 0),
                onClick = {
                    currentServiceIdx--
                }
            ) {
                Icon(
                    //modifier = Modifier.align(Alignment.Top),
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Next Service"
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = services[currentServiceIdx].name,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            IconButton(
                enabled = (currentServiceIdx != (totalServices - 1)),
                onClick = {
                    currentServiceIdx++
                }
            ) {
                Icon(
                    //modifier = Modifier.align(Alignment.Top),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next Service"
                )
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = services[currentServiceIdx].uuid,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )

        ServicePagerDetail(services[currentServiceIdx], onRead, onShowUserMessage)

    }
}


@Composable
fun ServicePagerDetail(
    service: DeviceService,
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {

        service.characteristics.forEach { char ->
            OutlinedCard(
                modifier = Modifier
                    .defaultMinSize(minHeight = 200.dp)
            ) {
                var state by remember { mutableStateOf(0) }
                var expanded by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier.padding(6.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column() {

                            Text(
                                text = char.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = char.uuid.uppercase(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        ReadWriteMenu(
                            expanded = expanded,
                            onExpanded = { expanded = it },
                            char = char,
                            onState = { state = it })
                    }

                    Spacer(modifier = Modifier.height(10.dp))


                    if (state == 0) {

                        ReadDeviceOptions(char, onRead, onShowUserMessage)

                    } else {
                        Text(text = "Write: ${char.canWrite}")

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
