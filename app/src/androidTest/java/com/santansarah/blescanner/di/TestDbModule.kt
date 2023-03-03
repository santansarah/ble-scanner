package com.santansarah.blescanner.di

import android.app.Application
import androidx.room.Room
import com.santansarah.blescanner.data.local.BleDao
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.room.TestBleDatabase
import org.koin.dsl.module
import timber.log.Timber

val testDatabaseModule = module {

    @Synchronized
    fun provideTestDataBase(
        application: Application
    ): TestBleDatabase {

        Timber.d("Creating new database instance....")

        return Room.inMemoryDatabaseBuilder(application, TestBleDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    fun provideBleDao(dataBase: TestBleDatabase): BleDao {
        return dataBase.bleDao()
    }

    /*scope(named("PER_METHOD")) {
        scoped { provideTestDataBase(get()) }
        factory { provideBleDao(get()) }
        factory { BleRepository(get()) }
    }*/

    scope<TestBleDatabase> {
        scoped { provideTestDataBase(get()) }
        scoped { provideBleDao(get()) }
        scoped { BleRepository(get()) }
    }

    //factory { provideBleDao(get()) }
    //factory { BleRepository(get()) }

}