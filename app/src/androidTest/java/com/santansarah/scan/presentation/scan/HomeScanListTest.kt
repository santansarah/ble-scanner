package com.santansarah.scan.presentation.scan

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.santansarah.scan.presentation.previewparams.PortraitListParams
import com.santansarah.scan.presentation.theme.SanTanScanTheme
import com.santansarah.scan.util.saveScreenshot
import de.mannodermaus.junit5.compose.createComposeExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class HomeScanListTest {

    @RegisterExtension
    @JvmField
    val extension = createComposeExtension()

    @OptIn(ExperimentalPermissionsApi::class)
    @Test
    fun homeLayout_Scanning() {

        val scanValues = PortraitListParams().values.first()

        extension.runComposeTest {
            setContent {
                SanTanScanTheme() {
                    HomeLayout(
                        appLayoutInfo = scanValues.appLayoutInfo,
                        scanState = scanValues.scanState,
                        multiplePermissionsState = object : MultiplePermissionsState {
                            override val allPermissionsGranted: Boolean
                                get() = true
                            override val permissions: List<PermissionState>
                                get() = TODO("Not yet implemented")
                            override val revokedPermissions: List<PermissionState>
                                get() = TODO("Not yet implemented")
                            override val shouldShowRationale: Boolean
                                get() = TODO("Not yet implemented")

                            override fun launchMultiplePermissionRequest() {
                                TODO("Not yet implemented")
                            }
                        },
                        appSnackBarHostState = remember { SnackbarHostState() },
                        isScanning = true,
                        isEditing = false,
                        startScan = { /*TODO*/ },
                        stopScan = { /*TODO*/ },
                        onControlClick = {},
                        onFilter ={},
                        onShowUserMessage ={}
                    )
                }
            }

            val bitmap = onRoot().captureToImage().asAndroidBitmap()
            saveScreenshot(
                "homeScanListPortrait"
                        + System.currentTimeMillis().toString(), bitmap
            )

        }
    }

}
