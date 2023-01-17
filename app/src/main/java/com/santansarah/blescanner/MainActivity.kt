package com.santansarah.blescanner

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
import com.santansarah.blescanner.presentation.BLEPermissions
import com.santansarah.blescanner.presentation.HomeRoute
import com.santansarah.blescanner.ui.theme.BLEScannerTheme
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber
import timber.log.Timber.Forest.plant


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bleManager = get<BLEManager>()

        setContent {
            BLEScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                   modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeRoute()
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