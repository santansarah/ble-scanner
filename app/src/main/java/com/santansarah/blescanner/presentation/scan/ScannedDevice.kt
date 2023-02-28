package com.santansarah.blescanner.presentation.scan

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.presentation.previewparams.devices
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.presentation.theme.labelSmallItalic
import com.santansarah.blescanner.utils.toDate

@Composable
fun ScannedDevice(
    device: ScannedDevice,
    onClick: (String) -> Unit,
    onFavorite: (ScannedDevice) -> Unit,
    onForget: (ScannedDevice) -> Unit
) {

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(device.address)
            },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Box(modifier = Modifier.fillMaxSize()) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.15f)
                        .padding(end = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.signal),
                        contentDescription = "RSSI Signal"
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = device.rssi.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "(${device.baseRssi})",
                        style = labelSmallItalic,
                        textAlign = TextAlign.Center
                    )
                }
                Column(modifier = Modifier.fillMaxWidth(.9f)) {
                    Text(
                        text = device.customName ?: device.deviceName
                        ?: "Unknown Name"
                    )
                    device.manufacturer?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    device.extra?.let {
                        Text(
                            text = it.joinToString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    device.services?.let {
                        Text(
                            text = it.joinToString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = device.address,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Last seen: ${device.lastSeen.toDate()}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    onForget(device)
                }) {
                Icon(
                    //modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.delete_forever),
                    contentDescription = "Forget",
                    tint = MaterialTheme.colorScheme.onSurface.copy(.5f)
                )
            }

            val favoriteIcon = if (device.favorite) R.drawable.favorite_selected
            else R.drawable.favorite_unselected

            IconButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { onFavorite(device) }) {
                Icon(
                    painter = painterResource(id = favoriteIcon),
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }


        }

    }

}

@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun ScannedDevicePreview() {
    val device = devices[0]
    BLEScannerTheme {
        Surface() {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                val backgroundImage = if (isSystemInDarkTheme())
                    painterResource(id = R.drawable.ble_background_dark)
                else
                    painterResource(id = R.drawable.ble_background)

                Image(
                    painter = backgroundImage,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )

                ScannedDevice(
                    device = device,
                    {}, {}, {}
                )
            }
        }
    }
}
