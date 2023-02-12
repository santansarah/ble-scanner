package com.santansarah.blescanner.presentation.scan.device

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.domain.models.DeviceCharacteristics
import com.santansarah.blescanner.domain.models.DeviceDescriptor
import com.santansarah.blescanner.presentation.theme.codeFont
import com.santansarah.blescanner.utils.ParsableCharacteristic
import timber.log.Timber

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ReadDescriptorOptions(
    charUuid: String,
    descriptor: DeviceDescriptor,
    onRead: (String, String) -> Unit,
    onShowUserMessage: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
        //.padding(6.dp)
    ) {
        FilledIconButton(
            //enabled = char.canRead,
            onClick = { onRead(charUuid, descriptor.uuid) })
        {
            Icon(
                painter = painterResource(
                    id = R.drawable.read_descriptor
                ),
                contentDescription = "Read"
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        val clipboardManager = LocalClipboardManager.current
        FilledIconButton(
            enabled = descriptor.readBytes != null,
            onClick = {
                clipboardManager.setText(
                    AnnotatedString(descriptor.getReadInfo())
                )
                onShowUserMessage("Data Copied.")
            })
        {
            Icon(
                modifier = Modifier.size(22.dp),
                painter = painterResource(id = R.drawable.copy),
                contentDescription = "Copy",
            )
        }
    }

    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(6.dp)
    ) {

        SelectionContainer {
            Column {
                Text(
                    text = descriptor.getReadInfo(),
                    style = codeFont
                )
            }
        }
    }

}
