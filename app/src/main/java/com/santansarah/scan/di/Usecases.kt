package com.santansarah.scan.di

import com.santansarah.scan.domain.usecases.ParseDescriptor
import com.santansarah.scan.domain.usecases.ParseNotification
import com.santansarah.scan.domain.usecases.ParseRead
import com.santansarah.scan.domain.usecases.ParseScanResult
import com.santansarah.scan.domain.usecases.ParseService
import org.koin.dsl.module

val usecasesModule = module {

    single { ParseScanResult(get()) }
    single { ParseService(get()) }
    single { ParseRead() }
    single { ParseNotification() }
    single { ParseDescriptor() }

}