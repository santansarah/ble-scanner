package com.santansarah.blescanner.util

import android.util.Log
import com.santansarah.blescanner.data.local.room.TestBleDatabase
import com.santansarah.sharedtest.companies
import com.santansarah.sharedtest.msDevices
import com.santansarah.sharedtest.services
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class SetUpKoinTest() : BeforeAllCallback, AfterAllCallback, KoinTest {

    private val bleDatabase: TestBleDatabase by inject()

    override fun beforeAll(context: ExtensionContext?) {

        with(bleDatabase.testDao()) {
            insertServices(services)
            insertCompanies(companies)
            insertMicrosoftDevices(msDevices)
        }

    }

    override fun afterAll(context: ExtensionContext?) {
        Log.d("debugTest", "Closing db...")
        bleDatabase.close()
    }
}