package com.santansarah.blescanner.di


import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.santansarah.blescanner.MainActivity
import com.santansarah.blescanner.domain.BLEManager
import com.santansarah.blescanner.presentation.scan.ScanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    fun provideBluetoohManager(app: Application): BluetoothManager {
        return app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    single<BluetoothAdapter> { provideBluetoohManager(androidApplication()).adapter }

    factory(named("IODispatcher")) {
        Dispatchers.IO + Job()
    }

    factory { CoroutineScope(get(named("IODispatcher"))) }

    single {  BLEManager(get(), get(), get()) }
    viewModel { ScanViewModel(get(), get(), Dispatchers.IO) }

}