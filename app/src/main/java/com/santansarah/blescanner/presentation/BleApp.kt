package com.santansarah.blescanner.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.santansarah.blescanner.R
import com.santansarah.blescanner.domain.models.AppRoutes.HOME_SCREEN

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

        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            AppNavGraph(navController = rememberNavController(),
                startDestination = HOME_SCREEN)
        }
    }

}