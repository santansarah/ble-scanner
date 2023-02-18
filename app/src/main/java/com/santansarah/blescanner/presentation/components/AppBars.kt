package com.santansarah.blescanner.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarWithBackButton(
    title: String,
    onBackClicked: () -> Unit
) {

    CenterAlignedTopAppBar(
        //modifier = Modifier.border(2.dp, Color.Blue),
        windowInsets = WindowInsets(
            top = 0.dp,
            bottom = 0.dp
        ),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF00005d),
            titleContentColor = Color(0xFFcaccd9),
            navigationIconContentColor = MaterialTheme.colorScheme
                .onPrimary.copy(.7f)
        ),
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                BackIcon(contentDesc = "Go Back")
            }
        }
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeAppBar(
    scanning: Boolean,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {

    TopAppBar(
        //modifier = Modifier.border(2.dp, Color.Blue),
        windowInsets = WindowInsets(
            top = 0.dp,
            bottom = 0.dp
        ),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF00005d),
            titleContentColor = Color(0xFFcaccd9),
            navigationIconContentColor = MaterialTheme.colorScheme
                .onPrimary.copy(.7f)
        ),
        title = {
            Text(
                text = if (scanning) "Your Devices" else "Scan Stopped",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                //style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.outline
                ),
                enabled = !scanning,
                onClick = { onStartScan() },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.connect),
                        contentDescription = "Connect"
                    )
                })
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.outline
                ),
                enabled = scanning,
                onClick = { onStopScan()  },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.disconnect),
                        contentDescription = "Disconnect"
                    )
                })
        }
    )
}



@Preview
@Composable
fun PreviewAppBar() {
    BLEScannerTheme() {
        AppBarWithBackButton(title = "ELK-BLEDOM") {
            
        }
    }
}

@Preview
@Composable
fun PreviewHomeBar() {
    BLEScannerTheme() {
        HomeAppBar(scanning = true, {}, {})
    }
}

