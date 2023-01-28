package com.santansarah.blescanner.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.sharedtest.deviceList
import com.santansarah.sharedtest.newDevice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
internal class Test : KoinTest {

    private val bleDb: TestBleDatabase =
        Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TestBleDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

    @AfterAll
    @Throws(IOException::class)
    fun closeDb() {
        bleDb.close()
    }

    @Test
    @DisplayName("Insert device and read from flow")
    fun test() {
        println(bleDb.toString())
    }
}