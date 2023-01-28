package com.santansarah.blescanner.di

import android.app.Application
import androidx.room.Room
import com.santansarah.blescanner.data.local.BleDao
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.TestBleDatabase
import com.santansarah.blescanner.data.local.TestDao
import org.koin.dsl.module

val testDatabaseModule = module {

    fun provideTestDataBase(
        application: Application
    ): TestBleDatabase {
        return Room.inMemoryDatabaseBuilder(application, TestBleDatabase::class.java)
            .build()
    }

    fun provideBleDao(dataBase: TestBleDatabase): BleDao {
        return dataBase.bleDao()
    }

    single { provideTestDataBase(get()) }
    single { provideBleDao(get()) }
    single { BleRepository(get()) }

}