package com.santansarah.scan.presentation.scan.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.santansarah.scan.R
import com.santansarah.scan.domain.bleparsables.CCCD
import com.santansarah.scan.domain.models.DeviceDescriptor
import com.santansarah.scan.domain.models.getReadInfo
import com.santansarah.scan.presentation.theme.codeFont

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ReadDescriptor(
    charUuid: String,
    descriptor: DeviceDescriptor,
    onRead: (String, String) -> Unit,
    onShowUserMessage: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .offset(y = (-8).dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.descriptor_buttons),
            contentDescription = "Descriptor Buttons",
            tint = MaterialTheme.colorScheme.onSecondary
        )
        IconButton(
            //enabled = char.canRead,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { onRead(charUuid, descriptor.uuid) })
        {
            Icon(
                painter = painterResource(
                    id = R.drawable.read_descriptor
                ),
                contentDescription = "Read"
            )
        }
        //Spacer(modifier = Modifier.width(2.dp))
        val clipboardManager = LocalClipboardManager.current
        IconButton(
            //enabled = char.canRead,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            ),
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

    val boxMinHeight = if (descriptor.uuid == CCCD.uuid)
        50.dp
    else
        100.dp

    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = boxMinHeight)
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
