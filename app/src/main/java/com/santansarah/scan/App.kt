package com.santansarah.scan

import android.app.Application
import com.santansarah.scan.di.appModule
import com.santansarah.scan.di.databaseModule
import com.santansarah.scan.di.usecasesModule
import com.santansarah.scan.utils.logging.DebugTree
import com.santansarah.scan.utils.logging.ReleaseTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class SanTanScanApp : Application() {
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
            androidContext(this@SanTanScanApp)
            workManagerFactory()
            // Load modules
            modules(appModule, databaseModule, usecasesModule)
        }

    }
}

