package com.santansarah.blescanner.presentation

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import com.santansarah.blescanner.utils.permissionsArray
import org.koin.core.component.KoinComponent
import timber.log.Timber

class PermissionManager(
    private val activity: ComponentActivity
): KoinComponent {

    private val registry: ActivityResultRegistry = activity.activityResultRegistry
    private var btEnableResultLauncher = registerLauncher(activity, "BlePermissions")

    private fun launchPermissionCheck() {
        btEnableResultLauncher.launch(permissionsArray)
    }

    private fun registerLauncher(owner: LifecycleOwner, key: String) = registry.register(
        key,
        owner,
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle Permission granted/rejected
        permissions.entries.forEach {
            Timber.d(it.toString())
            val permissionName = it.key
            val isGranted = it.value
            if (isGranted) {
                // Permission is granted
            } else {
                // Permission is denied
            }
        }

    }

}