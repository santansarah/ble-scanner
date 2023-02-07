package com.santansarah.blescanner.di


import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.santansarah.blescanner.presentation.BleGatt
import com.santansarah.blescanner.presentation.BleManager
import com.santansarah.blescanner.presentation.scan.ScanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    fun provideBluetoothManager(app: Application): BluetoothManager {
        return app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    single<BluetoothAdapter> { provideBluetoothManager(androidApplication()).adapter }

    factory(named("IODispatcher")) {
        Dispatchers.IO + Job()
    }

    factory { CoroutineScope(get(named("IODispatcher"))) }

    single {  BleManager(get(), get(), get()) }
    single { BleGatt(androidApplication(), get(), get(), get()) }
    viewModel { ScanViewModel(get(), get(), get(), Dispatchers.IO) }

}