package com.santansarah.blescanner.di


import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.santansarah.blescanner.domain.interfaces.IAnalytics
import com.santansarah.blescanner.presentation.BleGatt
import com.santansarah.blescanner.presentation.BleManager
import com.santansarah.blescanner.presentation.control.ControlViewModel
import com.santansarah.blescanner.presentation.scan.ScanViewModel
import com.santansarah.blescanner.utils.logging.Analytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single { Firebase.analytics }
    single<IAnalytics> { Analytics(get()) }

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

    viewModel { ScanViewModel(get(), get(), get(), get(named("IODispatcher")), get()) }
    viewModel { ControlViewModel(get(), get(), get(named("IODispatcher")), get()) }

}