package com.santansarah.blescanner.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme


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