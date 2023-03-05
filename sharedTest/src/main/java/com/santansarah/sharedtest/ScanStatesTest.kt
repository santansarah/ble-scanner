package com.santansarah.sharedtest

import com.santansarah.blescanner.domain.models.BleConnectEvents
import com.santansarah.blescanner.domain.models.BleReadWriteCommands
import com.santansarah.blescanner.domain.models.ConnectionState
import com.santansarah.blescanner.domain.models.DeviceEvents
import com.santansarah.blescanner.domain.models.ScanState
import com.santansarah.blescanner.domain.models.ScanUI
import com.santansarah.blescanner.presentation.previewparams.FeatureParams
import com.santansarah.blescanner.presentation.previewparams.devices
import com.santansarah.blescanner.presentation.previewparams.portrait

val scanStatePortrait = FeatureParams(ScanState(
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