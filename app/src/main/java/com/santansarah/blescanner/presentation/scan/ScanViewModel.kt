package com.santansarah.blescanner.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santansarah.blescanner.data.local.BleRepository
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.presentation.BleGatt
import com.santansarah.blescanner.presentation.BleManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class ScanViewModel(
    private val bleManager: BleManager,
    private val bleGatt: BleGatt,
    bleRepository: BleRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {

    val deviceList: StateFlow<List<ScannedDevice>> = bleRepository.getScannedDevices()
        .flowOn(dispatcher)
        //.onStart { emit("Loading...") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    fun startScan() {
        bleManager.scan()
    }

    fun connectToDevice(address: String) {
        bleGatt.connect(address)
    }

}