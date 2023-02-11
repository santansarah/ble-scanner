package com.santansarah.blescanner.di

import com.santansarah.blescanner.domain.usecases.ParseDescriptor
import com.santansarah.blescanner.domain.usecases.ParseNotification
import com.santansarah.blescanner.domain.usecases.ParseRead
import com.santansarah.blescanner.domain.usecases.ParseScanResult
import com.santansarah.blescanner.domain.usecases.ParseService
import org.koin.dsl.module

val usecasesModule = module {

    single { ParseScanResult(get()) }
    single { ParseService(get()) }
    single { ParseRead() }
    single { ParseNotification() }
    single { ParseDescriptor() }

}