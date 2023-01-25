package com.santansarah.blescanner.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import com.santansarah.blescanner.data.local.BleRepository
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber
import java.util.Timer

@SuppressLint("MissingPermission")
class BleGatt(
    private val app: Application,
    private val bleRepository: BleRepository,
    private val scope: CoroutineScope
) : KoinComponent {

    private var btGatt: BluetoothGatt? = null
    private val btAdapter: BluetoothAdapter = get()

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server
                gatt.discoverServices()
                btGatt = gatt
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // disconnected from the GATT Server
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                gatt?.services?.forEach {
                    it.includedServices.forEach { svc ->
                        Timber.i("service: $svc")
                    }
                    it.characteristics.forEach { char ->
                        Timber.d("char: $char")
                    }
                }
            } else {
                Timber.w("onServicesDiscovered received: $status")
            }
        }

    }

    fun connect(address: String) {
        btAdapter.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                device.connectGatt(app, false, bluetoothGattCallback)
            } catch (exception: IllegalArgumentException) {
                Timber.w("Device not found with provided address.")
            }
        }
    }

    private fun close() {
        btGatt?.let { gatt ->
            gatt.close()
            btGatt = null
        }
    }


}