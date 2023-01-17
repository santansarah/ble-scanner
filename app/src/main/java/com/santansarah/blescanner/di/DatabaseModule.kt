package com.santansarah.blescanner.di

import android.app.Application
import androidx.room.Room
import com.santansarah.blescanner.data.local.BleDao
import com.santansarah.blescanner.data.local.BleDatabase
import com.santansarah.blescanner.data.local.BleRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDataBase(application: Application): BleDatabase {
        return Room.databaseBuilder(application, BleDatabase::class.java, "ble")
            .fallbackToDestructiveMigration()
            .createFromAsset("database/ble.db")
            .build()
    }

    fun provideDao(dataBase: BleDatabase): BleDao {
        return dataBase.bleDao()
    }

    fun provideBleRepository(dao: BleDao): BleRepository = BleRepository(dao)

    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
    single { BleRepository(get()) }

}