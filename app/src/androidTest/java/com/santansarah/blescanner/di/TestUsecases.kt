package com.santansarah.blescanner.di

import com.santansarah.blescanner.domain.usecases.ParseScanResult
import org.koin.dsl.module

val testUsecasesModule = module {

    single { ParseScanResult(get()) }

}