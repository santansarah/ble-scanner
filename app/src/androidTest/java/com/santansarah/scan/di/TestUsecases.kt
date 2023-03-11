package com.santansarah.scan.di

import com.santansarah.scan.domain.usecases.ParseDescriptor
import com.santansarah.scan.domain.usecases.ParseNotification
import com.santansarah.scan.domain.usecases.ParseRead
import com.santansarah.scan.domain.usecases.ParseScanResult
import com.santansarah.scan.domain.usecases.ParseService
import com.santansarah.scan.local.room.TestBleDatabase
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

