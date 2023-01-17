package com.santansarah.blescanner.di

import com.santansarah.blescanner.domain.BLEManager
import org.koin.dsl.module

val appModule = module {

single { BLEManager(get(), get(), get()) }

}