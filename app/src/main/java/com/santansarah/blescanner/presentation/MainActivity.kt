package com.santansarah.blescanner.presentation

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.santansarah.blescanner.domain.DeleteNotSeenWorker
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import org.koin.android.ext.android.get
import org.koin.androidx.compose.get
import org.koin.core.context.GlobalContext.get
import org.koin.core.qualifier.named
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
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

        setContent {
            BLEScannerTheme {
                BleApp()
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