package com.santansarah.blescanner.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.santansarah.blescanner.presentation.BleObserver
import com.santansarah.blescanner.presentation.scan.HomeRoute
import com.santansarah.blescanner.ui.theme.BLEScannerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bleObserver = BleObserver(this)
        this.lifecycle.addObserver(bleObserver)

        setContent {
            BLEScannerTheme {
                BleApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BLEScannerTheme {
        //Greeting("Android")
    }
}