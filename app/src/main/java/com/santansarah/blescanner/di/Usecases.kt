package com.santansarah.blescanner.di

import com.santansarah.blescanner.domain.usecases.ParseScanResult
import org.koin.dsl.module

val usecasesModule = module {

    single { ParseScanResult(get()) }


}