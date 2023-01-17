package com.santansarah.blescanner.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatcher = module {

    factory { CoroutineScope(get(named("IODispatcher"))) }

    factory(named("IODispatcher")) {
        Dispatchers.IO + Job()
    }

}