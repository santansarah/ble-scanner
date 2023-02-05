package com.santansarah.blescanner.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.presentation.scan.HomeRoute

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BleApp(
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
        val layoutDirection = LocalLayoutDirection.current
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .padding(
                    top = systemBarsPadding.calculateTopPadding(),
                    bottom = systemBarsPadding.calculateBottomPadding(),
                    start = systemBarsPadding.calculateStartPadding(layoutDirection),
                    end = systemBarsPadding.calculateEndPadding(layoutDirection)
                )
                .fillMaxSize()
        ) {
            HomeRoute()
        }
    }

}