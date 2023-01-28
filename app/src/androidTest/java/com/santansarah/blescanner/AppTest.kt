package com.santansarah.blescanner

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnitRunner
import com.santansarah.blescanner.di.testAppModule
import com.santansarah.blescanner.di.testDatabaseModule
import com.santansarah.blescanner.di.testUsecasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BLEScannerAppTest : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(ApplicationProvider.getApplicationContext())
            // Load modules
            modules(
                testAppModule, testDatabaseModule,
                testUsecasesModule
            )
        }

    }
}

class InstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader,
            BLEScannerAppTest::class.java.name, context)
    }
}