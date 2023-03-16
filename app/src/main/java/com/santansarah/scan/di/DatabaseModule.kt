package com.santansarah.scan.di

import android.app.Application
import androidx.room.Room
import com.santansarah.scan.domain.interfaces.IBleRepository
import com.santansarah.scan.local.BleDao
import com.santansarah.scan.local.BleDatabase
import com.santansarah.scan.local.BleRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDataBase(application: Application): BleDatabase {
        return Room.databaseBuilder(application, BleDatabase::class.java, "ble")
            .createFromAsset("database/ble.db")
            .build()
    }

    fun provideDao(dataBase: BleDatabase): BleDao {
        return dataBase.bleDao()
    }

    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
    single<IBleRepository> { BleRepository(get()) }

}