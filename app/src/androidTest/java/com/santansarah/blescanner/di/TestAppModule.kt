package com.santansarah.blescanner.di

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.santansarah.blescanner.presentation.BleGatt
import com.santansarah.blescanner.presentation.BleManager
import com.santansarah.blescanner.presentation.scan.ScanViewModel
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val testAppModule = module {

    single<Application> { ApplicationProvider.getApplicationContext() }

/*
    fun provideBluetoothManager(app: Application): BluetoothManager {
        return app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
*/

    single { mockkStatic(BluetoothAdapter::class) }

    factory(named("IODispatcher")) {
        StandardTestDispatcher() + Job()
    }

    factory { TestScope(get(named("IODispatcher"))) }

    single {  BleManager(get(), get(), get(), get()) }
    //single { BleGatt(androidApplication(), get(), get()) }
    viewModel { ScanViewModel(get(), get(), get(), StandardTestDispatcher()) }

}