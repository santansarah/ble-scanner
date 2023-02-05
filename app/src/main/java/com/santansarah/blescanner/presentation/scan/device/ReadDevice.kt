package com.santansarah.blescanner.presentation.scan.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.presentation.theme.codeFont
import com.santansarah.blescanner.utils.ParsableCharacteristic
import timber.log.Timber

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ReadDeviceOptions(
    char: DeviceCharacteristics,
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
        //.padding(6.dp)
    ) {
        AssistChip(
            enabled = char.canRead,
            label = { Text(text = "Get Data") },
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.outline_arrow_circle_down
                    ),
                    contentDescription = "Read Data"
                )
            },
            onClick = { onRead(char.uuid) })
        Spacer(modifier = Modifier.width(10.dp))
        val clipboardManager = LocalClipboardManager.current
        AssistChip(
            enabled = char.readBytes != null,
            label = { Text(text = "Copy") },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.copy),
                    contentDescription = "Read Data"
                )
            },
            onClick = {
                clipboardManager.setText(
                    AnnotatedString(char.getReadInfo())
                )
                onShowUserMessage("Data Copied.")
            })
    }

    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(6.dp)
    ) {
        SelectionContainer {
            Column {
                Timber.d(char.uuid)
                Timber.d(ParsableCharacteristic.Appearance.uuid)

                Text(
                    text = char.getReadInfo(),
                    style = codeFont
                )
            }
        }
    }
}
