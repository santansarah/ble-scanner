package com.santansarah.blescanner.di

import com.santansarah.blescanner.data.local.room.TestBleDatabase
import com.santansarah.blescanner.domain.usecases.ParseDescriptor
import com.santansarah.blescanner.domain.usecases.ParseNotification
import com.santansarah.blescanner.domain.usecases.ParseRead
import com.santansarah.blescanner.domain.usecases.ParseScanResult
import com.santansarah.blescanner.domain.usecases.ParseService
import org.koin.dsl.module

val testUsecasesModule = module {

    scope<TestBleDatabase> {
        scoped { ParseScanResult(get()) }
        scoped { ParseService(get()) }
    }

    single { ParseRead() }
    single { ParseNotification() }
    single { ParseDescriptor() }
}

