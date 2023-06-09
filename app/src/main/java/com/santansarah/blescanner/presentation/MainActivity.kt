package com.santansarah.blescanner.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.windowinfo.getFoldableInfoFlow
import com.santansarah.blescanner.utils.windowinfo.getWindowLayoutType
import com.santansarah.blescanner.utils.windowinfo.getWindowSizeClasses
import com.santansarah.scan.presentation.BleObserver


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bleObserver = BleObserver(this)
        this.lifecycle.addObserver(bleObserver)

        //val deleteNotSeenRequest: WorkRequest = get(named("DeleteNotSeenWorker"))

/*
        WorkManager
            .getInstance(this)
            .enqueue(deleteNotSeenRequest)
*/

        val devicePostureFlow = getFoldableInfoFlow(this)

        setContent {

            val windowSize = getWindowSizeClasses(this)
            val devicePosture by devicePostureFlow.collectAsStateWithLifecycle()

            val appLayoutInfo = getWindowLayoutType(
                windowInfo = windowSize,
                foldableInfo = devicePosture
            )

            BLEScannerTheme {
                BleApp(appLayoutInfo)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BLEScannerTheme {
        TextField(value = "Test", onValueChange = {})
    }
}