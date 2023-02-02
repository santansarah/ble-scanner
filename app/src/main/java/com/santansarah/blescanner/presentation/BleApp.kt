package com.santansarah.blescanner.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.presentation.scan.HomeRoute

@Composable
fun BleApp(
) {

    Surface(
        //modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
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

            val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
            Column(
                modifier = Modifier.padding(start = 14.dp,
                    end = 14.dp,
                top = systemBarsPadding.calculateTopPadding() + 14.dp,
                bottom = systemBarsPadding.calculateBottomPadding() + 14.dp)
                    .fillMaxSize()
            ) {
                HomeRoute()
            }
        }
    }

}