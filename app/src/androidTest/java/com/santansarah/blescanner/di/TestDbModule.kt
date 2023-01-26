package com.santansarah.blescanner.di

import android.app.Application
import androidx.room.Room
import com.santansarah.blescanner.data.local.BleDao
import com.santansarah.blescanner.data.local.BleDatabase
import com.santansarah.blescanner.data.local.BleRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val testDatabaseModule = module {

    fun provideTestDataBase(application: Application): BleDatabase {
        return Room.inMemoryDatabaseBuilder(application, BleDatabase::class.java)
            .build()
    }

    fun provideTestDao(dataBase: BleDatabase): BleDao {
        return dataBase.bleDao()
    }

    factory { provideTestDataBase(get()) }
    factory { provideTestDao(get()) }
    factory { BleRepository(get()) }

}