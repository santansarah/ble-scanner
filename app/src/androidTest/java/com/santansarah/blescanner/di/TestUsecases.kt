package com.santansarah.blescanner.di

import com.santansarah.blescanner.data.local.room.TestBleDatabase
import com.santansarah.blescanner.domain.usecases.ParseScanResult
import org.koin.dsl.module

val testUsecasesModule = module {

    scope<TestBleDatabase> {
        scoped { ParseScanResult(get()) }
    }

}