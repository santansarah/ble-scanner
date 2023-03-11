package com.santansarah.sharedtest

import com.santansarah.scan.domain.models.BleConnectEvents
import com.santansarah.scan.domain.models.BleReadWriteCommands
import com.santansarah.scan.domain.models.ConnectionState
import com.santansarah.scan.domain.models.DeviceEvents
import com.santansarah.scan.domain.models.ScanState
import com.santansarah.scan.domain.models.ScanUI
import com.santansarah.scan.presentation.previewparams.FeatureParams
import com.santansarah.scan.presentation.previewparams.devices
import com.santansarah.scan.presentation.previewparams.portrait

val scanStatePortrait = FeatureParams(
    ScanState(
    ScanUI(
        devices,
        null,
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