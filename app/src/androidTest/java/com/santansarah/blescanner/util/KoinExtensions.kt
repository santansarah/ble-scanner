package com.santansarah.blescanner.util

import android.util.Log
import com.santansarah.blescanner.data.local.TestBleDatabase
import com.santansarah.sharedtest.companies
import com.santansarah.sharedtest.msDevices
import com.santansarah.sharedtest.services
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.inject

class SetUpKoinTest() : BeforeAllCallback, AfterAllCallback, KoinTest {

    private val bleDatabase: TestBleDatabase by inject()

    override fun beforeAll(context: ExtensionContext?) {

        val beforeInsert = bleDatabase.query("SELECT * from companies", null)
        Log.d("debugTest", beforeInsert.count.toString())

        bleDatabase.testDao().insertServices(services)
        bleDatabase.testDao().insertCompanies(companies)
        bleDatabase.testDao().insertMicrosoftDevices(msDevices)

        val afterInsert = bleDatabase.query("SELECT * from companies", null)
        Log.d("debugTest", afterInsert.count.toString())
    }

    override fun afterAll(context: ExtensionContext?) {
        Log.d("debugTest", "Closing db...")
        bleDatabase.close()
        stopKoin()
    }
}

class AddKoinModulesPerMethod(private vararg val modules: Module = emptyArray()) :
    BeforeEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        loadKoinModules(modules.asList())
    }
}