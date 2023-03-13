package com.santansarah.scan

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnitRunner
import com.santansarah.scan.di.testAppModule
import com.santansarah.scan.di.testDatabaseModule
import com.santansarah.scan.di.testUsecasesModule
import com.santansarah.scan.utils.logging.DebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SanTanScanAppTest : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(ApplicationProvider.getApplicationContext())

            Timber.d("Loading Test Koin modules...")

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
        return super.newApplication(
            classLoader,
            SanTanScanAppTest::class.java.name, context
        )
    }
}