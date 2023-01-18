package com.santansarah.blescanner

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.santansarah.blescanner.domain.BLEManager
import com.santansarah.blescanner.domain.BleObserver
import com.santansarah.blescanner.presentation.scan.HomeRoute
import com.santansarah.blescanner.ui.theme.BLEScannerTheme
import org.koin.android.ext.android.get


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bleObserver = BleObserver(this)
        this.lifecycle.addObserver(bleObserver)

        setContent {
            BLEScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeRoute(
                        isScanning = bleObserver.bleManager.isScanning
                    )
                }
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