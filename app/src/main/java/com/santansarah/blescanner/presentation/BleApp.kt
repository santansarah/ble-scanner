package com.santansarah.blescanner.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.presentation.scan.HomeRoute

@Composable
fun BleApp(
    isScanning: Boolean
) {

    Surface(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            BLEPermissions()
            HomeRoute(isScanning = isScanning)
        }
    }

}