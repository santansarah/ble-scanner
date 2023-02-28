package com.santansarah.blescanner.presentation.previewparams

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.santansarah.blescanner.domain.models.BleConnectEvents
import com.santansarah.blescanner.domain.models.BleReadWriteCommands
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceEvents
import com.santansarah.blescanner.domain.models.ScanState
import com.santansarah.blescanner.domain.models.ScanUI
class DevicePortraitParams : PreviewParameterProvider<FeatureParams> {

    override val values = sequenceOf(
        FeatureParams(
            ScanState(
                ScanUI(
                    emptyList(),
                    deviceDetail,
                    ConnectionState.CONNECTING,
                    null,
                    null),
                BleConnectEvents({}, {}),
                BleReadWriteCommands(
                    {},
                    { _: String, _: String -> },
                    { _: String, _: String -> },
                    { _: String, _: String, _: String -> },
                ),
                DeviceEvents({}, {}, {},{},{})
            ),
            devices,
            deviceDetail,
            portrait
        )
    )
}
class DeviceLandscapeParams : PreviewParameterProvider<FeatureParams> {

    override val values = sequenceOf(
        FeatureParams(
            ScanState(
                ScanUI(
                    emptyList(),
                    deviceDetail,
                    ConnectionState.CONNECTING,
                    null,
                    null),
                BleConnectEvents({}, {}),
                BleReadWriteCommands(
                    {},
                    { _: String, _: String -> },
                    { _: String, _: String -> },
                    { _: String, _: String, _: String -> },
                ),
                DeviceEvents({}, {}, {},{},{})
            ),
            devices,
            deviceDetail,
            landscapeNormal
        )
    )
}
class DeviceBigParams : PreviewParameterProvider<FeatureParams> {

    override val values = sequenceOf(
        FeatureParams(
            ScanState(
                ScanUI(
                    emptyList(),
                    deviceDetail,
                    ConnectionState.CONNECTING,
                    null,
                    null),
                BleConnectEvents({}, {}),
                BleReadWriteCommands(
                    {},
                    { _: String, _: String -> },
                    { _: String, _: String -> },
                    { _: String, _: String, _: String -> },
                ),
                DeviceEvents({}, {}, {},{},{})
            ),
            devices,
            deviceDetail,
            landscapeBig
        )
    )
}
