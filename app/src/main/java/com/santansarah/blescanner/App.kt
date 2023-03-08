package com.santansarah.blescanner

import android.app.Application
import com.santansarah.blescanner.di.appModule
import com.santansarah.blescanner.di.databaseModule
import com.santansarah.blescanner.di.usecasesModule
import com.santansarah.blescanner.utils.logging.DebugTree
import com.santansarah.blescanner.utils.logging.ReleaseTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class BLEScannerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else
            Timber.plant(ReleaseTree())

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@BLEScannerApp)
            workManagerFactory()
            // Load modules
            modules(appModule, databaseModule, usecasesModule)
        }

    }
}

