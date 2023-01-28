package com.santansarah.blescanner.data.local

import com.santansarah.blescanner.data.local.room.TestBleDatabase
import com.santansarah.blescanner.util.SetUpKoinTest
import com.santansarah.sharedtest.companies
import com.santansarah.sharedtest.msDevices
import com.santansarah.sharedtest.services
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import org.koin.test.inject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(SetUpKoinTest::class)
internal class PrepopulatedRepositoryTest : KoinTest {

    private val bleDb by inject<TestBleDatabase>()
    private val bleRepository by inject<BleRepository>()

    @AfterAll
    fun closeDb() {
        bleDb.close()
    }

    @Test
    @DisplayName("Get company by id")
    fun getCompanyById() = runTest {
        val company = bleRepository.getCompanyById(companies[1].code)
        assertEquals(companies[1].code, company?.code)
    }

    @Test
    @DisplayName("Get service by id")
    fun getServiceById() = runTest {
        val service = bleRepository.getServiceById(services.first().uuid)
        assertEquals(services.first().uuid, service?.uuid)
    }

    @Test
    @DisplayName("Get Microsoft device by id")
    fun getMicrosoftDeviceById() = runTest {
        val msDevice = bleRepository.getMicrosoftDeviceById(msDevices.first().id)
        assertEquals(msDevices.first().id, msDevice?.id)
    }

}