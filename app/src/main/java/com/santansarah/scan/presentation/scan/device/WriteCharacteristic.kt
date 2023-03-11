package com.santansarah.scan.presentation.scan.device


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.santansarah.scan.R
import com.santansarah.scan.domain.models.DeviceCharacteristics
import com.santansarah.scan.domain.models.getWriteCommands
import com.santansarah.scan.presentation.theme.codeFont
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteCharacteristic(
    char: DeviceCharacteristics,
    onWrite: (String, String) -> Unit
) {
    Column {

        var customHexToWrite by rememberSaveable {
            mutableStateOf("")
        }

        var wroteHex by rememberSaveable {
            mutableStateOf("")
        }

        val listItems = char.getWriteCommands()
        if (listItems.isNotEmpty()) {
            Text(
                text = "Hints:",
                style = MaterialTheme.typography.labelLarge
            )
            SelectionContainer {
                Column {
                    listItems.forEach {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            enabled = char.canWrite,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            label = {
                Text(
                    text = "Hex String",
                    style = codeFont
                )
            },
            value = customHexToWrite,
            onValueChange = { customHexToWrite = it },
            singleLine = false,
            maxLines = 3,
            textStyle = codeFont,
            trailingIcon = {
                IconButton(onClick = { customHexToWrite = "" }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp),
        )

        if (wroteHex.isNotEmpty()) {

            val sdf = SimpleDateFormat.getDateTimeInstance()
            val currentDateAndTime = sdf.format(Date())

            Text(
                text = "Wrote: $wroteHex on $currentDateAndTime",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        AssistChip(
            enabled = char.canWrite,
            label = { Text(text = "Write") },
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.write_data
                    ),
                    contentDescription = "Write"
                )
            },
            onClick = {
                onWrite(
                    char.uuid,
                    customHexToWrite.substringAfter("0x")
                )
                wroteHex = customHexToWrite
                customHexToWrite = ""
            })

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ShowPreFilledOptions(
    listItems: Array<String>,
    hexToWrite: String,
    onDropdownChanged: (String) -> Unit
) {
    //var selectedWriteItem by rememberSaveable { mutableStateOf(listItems.first()) }
    var writeMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "Send common command:",
        style = MaterialTheme.typography.bodySmall
    )

    ExposedDropdownMenuBox(
        expanded = writeMenuExpanded,
        onExpandedChange = { writeMenuExpanded = !writeMenuExpanded }
    ) {

        TextField(
            modifier = Modifier.menuAnchor(),
            value = hexToWrite,
            onValueChange = {},
            //readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = writeMenuExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = writeMenuExpanded,
            onDismissRequest = {
                writeMenuExpanded = false
            }
        ) {
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    onDropdownChanged(selectedOption)
                    writeMenuExpanded = false
                },
                    text = {
                        Text(text = selectedOption)
                    }
                )
            }
        }
    }
}
