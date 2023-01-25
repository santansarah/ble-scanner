package com.santansarah.blescanner.presentation

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class BleObserver(
    private val activity: ComponentActivity
) : DefaultLifecycleObserver, KoinComponent {

    val bleManager = get<BleManager>()
    val btAdapter = get<BluetoothAdapter>()

    private val registry: ActivityResultRegistry = activity.activityResultRegistry
    lateinit var btEnableResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        createBroadcastReceiver()
        btEnableResultLauncher = registerHandler(owner, "EnableBLE")

        ContextCompat.registerReceiver(activity, broadcastReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED),
        ContextCompat.RECEIVER_EXPORTED)

    }

    private fun createBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {

                val action = intent.action

                // It means the user has changed their bluetooth state.
                if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                    if (btAdapter.state == BluetoothAdapter.STATE_OFF) {
                        bleManager.stopScan()
                        val btEnableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        btEnableResultLauncher.launch(btEnableIntent)
                        return
                    }
                    if (btAdapter.state == BluetoothAdapter.STATE_ON) {
                        bleManager.scan()
                        return
                    }
                }
            }
        }
    }

    private fun registerHandler(owner: LifecycleOwner, key: String) = registry.register(
        key,
        owner,
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            bleManager.scan()
        }
    }


}