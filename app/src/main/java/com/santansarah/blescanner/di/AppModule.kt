package com.santansarah.blescanner.di


import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import com.santansarah.blescanner.domain.DeleteNotSeenWorker
import com.santansarah.blescanner.presentation.BleGatt
import com.santansarah.blescanner.presentation.BleManager
import com.santansarah.blescanner.presentation.control.ControlViewModel
import com.santansarah.blescanner.presentation.scan.ScanViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {

    worker { DeleteNotSeenWorker(get(), get(), get(), get(named("IODispatcher"))) }

    single<WorkRequest>(named("DeleteNotSeenWorker")) {
        PeriodicWorkRequestBuilder<DeleteNotSeenWorker>(15, TimeUnit.MINUTES)
            //.setInitialDelay(Duration.ofMinutes(1))
            .build()
    }

    fun provideBluetoothManager(app: Application): BluetoothManager {
        return app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    single<BluetoothAdapter> { provideBluetoothManager(androidApplication()).adapter }

    factory(named("IODispatcher")) {
        Dispatchers.IO
    }

    factory { CoroutineScope(get<CoroutineDispatcher>(named("IODispatcher")) + Job()) }

    single { BleManager(get(), get(), get()) }
    single { BleGatt(androidApplication(), get(), get(), get(), get(), get()) }

    viewModel { ScanViewModel(get(), get(), get(), get(named("IODispatcher"))) }
    viewModel { ControlViewModel(get(), get(), get(named("IODispatcher")), get()) }

}