package com.santansarah.blescanner.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.koin.dsl.module

val testAppModule = module {

    single<Context> { ApplicationProvider.getApplicationContext() }

}