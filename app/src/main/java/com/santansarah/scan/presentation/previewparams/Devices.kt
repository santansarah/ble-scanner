package com.santansarah.scan.presentation.previewparams

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.domain.bleparsables.CCCD
import com.santansarah.scan.domain.models.BleConnectEvents
import com.santansarah.scan.domain.models.BleProperties
import com.santansarah.scan.domain.models.BleReadWriteCommands
import com.santansarah.scan.domain.models.BleWriteTypes
import com.santansarah.scan.domain.models.ConnectionState
import com.santansarah.scan.domain.models.DeviceCharacteristics
import com.santansarah.scan.domain.models.DeviceDescriptor
import com.santansarah.scan.domain.models.DeviceDetail
import com.santansarah.scan.domain.models.DeviceEvents
import com.santansarah.scan.domain.models.DeviceService
import com.santansarah.scan.domain.models.ScanState
import com.santansarah.scan.domain.models.ScanUI
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo
import com.santansarah.scan.utils.windowinfo.AppLayoutMode

data class FeatureParams(
    val scanState: ScanState,
    val scannedDevice: List<ScannedDevice>,
    val detail: DeviceDetail,
    val appLayoutInfo: AppLayoutInfo
)

class PortraitLayoutParams : PreviewParameterProvider<FeatureParams> {

    override val values = sequenceOf(
        FeatureParams(
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
        ),
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

class PortraitNarrowLayoutParams : PreviewParameterProvider<FeatureParams> {

    override val values = sequenceOf(
        FeatureParams(
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
            portraitNarrow
        ),
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
            portraitNarrow
        )
    )
}
class LandscapeLayoutParams : PreviewParameterProvider<FeatureParams> {

    override val values = sequenceOf(
        FeatureParams(
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
            landscapeNormal
        ),
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
class LandscapeBigLayoutParams : PreviewParameterProvider<FeatureParams> {

    override val values = sequenceOf(
        FeatureParams(
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
            landscapeBig
        ),
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


class PortraitPreviewParams : PreviewParameterProvider<FeatureParams> {

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
            AppLayoutInfo(
                appLayoutMode = AppLayoutMode.PORTRAIT,
                windowDpSize = DpSize(392.dp, 850.dp),
                foldableInfo = null
            )
        )
    )
}

class LandscapePreviewParams : PreviewParameterProvider<FeatureParams> {

    val devices = listOf(
        ScannedDevice(
            deviceId = 41,
            deviceName = "EASYWAY-BLE",
            address = "93:58:00:27:XX:00",
            rssi = -93,
            manufacturer = "Ericsson Technology Licensing",
            services = listOf("Heart Rate"),
            extra = null,
            lastSeen = 1675293173796,
            customName = null,
            baseRssi = -55,
            favorite = false,
            forget = false
        ),
        ScannedDevice(
            deviceId = 0,
            deviceName = "ELK-BLEDOM",
            address = "BE:00:FA:00:XX:00",
            rssi = -77,
            manufacturer = null,
            services = listOf("[Human Interface Device"),
            extra = null,
            lastSeen = 1674510398719,
            customName = null,
            baseRssi = -88,
            favorite = false,
            forget = false
        )
    )
    val deviceDetail = DeviceDetail(
        scannedDevice =
        ScannedDevice(
            deviceId = 41,
            deviceName = "EASYWAY-BLE",
            address = "93:58:00:27:XX:00",
            rssi = -93,
            manufacturer = "Ericsson Technology Licensing",
            services = listOf("Heart Rate"),
            extra = null,
            lastSeen = 1675293173796,
            customName = null,
            baseRssi = -55,
            favorite = false,
            forget = false
        ),
        services = listOf(
            DeviceService(
                uuid = "1800",
                name = "Generic Access",
                characteristics = listOf(
                    DeviceCharacteristics(
                        uuid = "00002a00-0000-1000-8000-00805f9b34fb",
                        name = "Device Name",
                        descriptor = null,
                        permissions = 0,
                        properties = listOf(BleProperties.PROPERTY_READ),
                        writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                        descriptors = listOf(
                            DeviceDescriptor(
                                CCCD.uuid,
                                "Client Characteristic Configuration", "",
                                emptyList(), BleProperties.PROPERTY_INDICATE, null
                            )
                        ),
                        canRead = true,
                        canWrite = false,
                        readBytes = null,
                        notificationBytes = null
                    ),
                    DeviceCharacteristics(
                        uuid = "00002a00-0000-1000-8000-00805f9b34fb",
                        name = "Appearance",
                        descriptor = null,
                        permissions = 0,
                        properties = listOf(BleProperties.PROPERTY_READ),
                        writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                        descriptors = emptyList(),
                        canRead = true,
                        canWrite = false,
                        readBytes = null,
                        notificationBytes = null
                    )
                )
            ),
            DeviceService(
                uuid = "1801",
                name = "Generic Attribute",
                characteristics = listOf(
                    DeviceCharacteristics(
                        uuid = "00002a05-0000-1000-8000-00805f9b34fb",
                        name = "Service Changed",
                        descriptor = null,
                        permissions = 0,
                        properties = listOf(BleProperties.PROPERTY_READ),
                        writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                        descriptors = emptyList(),
                        canRead = true,
                        canWrite = false,
                        readBytes = byteArrayOf(-60, 3),
                        notificationBytes = null
                    )
                )
            ),
            DeviceService(
                uuid = "0000ae00-0000-1000-8000-00805f9b34fb",
                name = "Mfr Service",
                characteristics = listOf(
                    DeviceCharacteristics(
                        uuid = "0000fff3-0000-1000-8000-00805f9b34fb",
                        name = "Mfr Characteristic",
                        descriptor = null,
                        permissions = 0,
                        properties = listOf(
                            BleProperties.PROPERTY_READ,
                            BleProperties.PROPERTY_WRITE
                        ),
                        writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                        descriptors = emptyList(),
                        canRead = false,
                        canWrite = true,
                        readBytes = null,
                        notificationBytes = null
                    ),
                    DeviceCharacteristics(
                        uuid = "0000ae02-0000-1000-8000-00805f9b34fb",
                        name = "Mfr Characteristic",
                        descriptor = null,
                        permissions = 0,
                        properties = listOf(BleProperties.PROPERTY_READ),
                        writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                        descriptors = emptyList(),
                        canRead = true,
                        canWrite = false,
                        readBytes = null,
                        notificationBytes = null
                    ),
                    DeviceCharacteristics(
                        uuid = "0000ae03-0000-1000-8000-00805f9b34fb",
                        name = "Mfr Characteristic",
                        descriptor = null,
                        permissions = 0,
                        properties = listOf(
                            BleProperties.PROPERTY_READ,
                            BleProperties.PROPERTY_WRITE
                        ),
                        writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                        descriptors = emptyList(),
                        canRead = false,
                        canWrite = false,
                        readBytes = null,
                        notificationBytes = null
                    )
                )
            )
        )
    )

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
            AppLayoutInfo(
                appLayoutMode = AppLayoutMode.LANDSCAPE_NORMAL,
                windowDpSize = DpSize(850.dp, 392.dp),
                foldableInfo = null
            )
        ),
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
            AppLayoutInfo(
                appLayoutMode = AppLayoutMode.LANDSCAPE_BIG,
                windowDpSize = DpSize(850.dp, 392.dp),
                foldableInfo = null
            )
        )
    )
}


class ScannedDevicePreviewParameterProvider : PreviewParameterProvider<ScannedDevice> {
    override val values = sequenceOf(
        ScannedDevice(
            deviceId = 41,
            deviceName = "EASYWAY-BLE",
            address = "93:58:00:27:XX:00",
            rssi = -93,
            manufacturer = "Ericsson Technology Licensing",
            services = listOf("Heart Rate"),
            extra = null,
            lastSeen = 1675293173796,
            customName = null,
            baseRssi = -55,
            favorite = false,
            forget = false
        )
    )
}

class PreviewDeviceDetailProvider : PreviewParameterProvider<DeviceDetail> {
    override val values = sequenceOf(
        DeviceDetail(
            scannedDevice =
            ScannedDevice(
                deviceId = 41,
                deviceName = "EASYWAY-BLE",
                address = "93:58:00:27:XX:00",
                rssi = -93,
                manufacturer = "Ericsson Technology Licensing",
                services = listOf("Heart Rate"),
                extra = null,
                lastSeen = 1675293173796,
                customName = null,
                baseRssi = -55,
                favorite = false,
                forget = false
            ),
            services = listOf(
                DeviceService(
                    uuid = "1800",
                    name = "Generic Access",
                    characteristics = listOf(
                        DeviceCharacteristics(
                            uuid = "00002a00-0000-1000-8000-00805f9b34fb",
                            name = "Device Name",
                            descriptor = null,
                            permissions = 0,
                            properties = listOf(BleProperties.PROPERTY_READ),
                            writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                            descriptors = listOf(
                                DeviceDescriptor(
                                    CCCD.uuid,
                                    "Client Characteristic Configuration", "",
                                    emptyList(), BleProperties.PROPERTY_INDICATE, null
                                )
                            ),
                            canRead = true,
                            canWrite = false,
                            readBytes = null,
                            notificationBytes = null
                        ),
                        DeviceCharacteristics(
                            uuid = "00002a00-0000-1000-8000-00805f9b34fb",
                            name = "Appearance",
                            descriptor = null,
                            permissions = 0,
                            properties = listOf(BleProperties.PROPERTY_READ),
                            writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                            descriptors = emptyList(),
                            canRead = true,
                            canWrite = false,
                            readBytes = null,
                            notificationBytes = null
                        )
                    )
                ),
                DeviceService(
                    uuid = "1801",
                    name = "Generic Attribute",
                    characteristics = listOf(
                        DeviceCharacteristics(
                            uuid = "00002a05-0000-1000-8000-00805f9b34fb",
                            name = "Service Changed",
                            descriptor = null,
                            permissions = 0,
                            properties = listOf(BleProperties.PROPERTY_READ),
                            writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                            descriptors = emptyList(),
                            canRead = true,
                            canWrite = false,
                            readBytes = byteArrayOf(-60, 3),
                            notificationBytes = null
                        )
                    )
                ),
                DeviceService(
                    uuid = "0000ae00-0000-1000-8000-00805f9b34fb",
                    name = "Mfr Service",
                    characteristics = listOf(
                        DeviceCharacteristics(
                            uuid = "0000ae01-0000-1000-8000-00805f9b34fb",
                            name = "Mfr Characteristic",
                            descriptor = null,
                            permissions = 0,
                            properties = listOf(
                                BleProperties.PROPERTY_READ,
                                BleProperties.PROPERTY_WRITE
                            ),
                            writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                            descriptors = emptyList(),
                            canRead = false,
                            canWrite = true,
                            readBytes = null,
                            notificationBytes = null
                        ),
                        DeviceCharacteristics(
                            uuid = "0000ae02-0000-1000-8000-00805f9b34fb",
                            name = "Mfr Characteristic",
                            descriptor = null,
                            permissions = 0,
                            properties = listOf(BleProperties.PROPERTY_READ),
                            writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                            descriptors = emptyList(),
                            canRead = true,
                            canWrite = false,
                            readBytes = null,
                            notificationBytes = null
                        ),
                        DeviceCharacteristics(
                            uuid = "0000ae03-0000-1000-8000-00805f9b34fb",
                            name = "Mfr Characteristic",
                            descriptor = null,
                            permissions = 0,
                            properties = listOf(
                                BleProperties.PROPERTY_READ,
                                BleProperties.PROPERTY_WRITE
                            ),
                            writeTypes = listOf(BleWriteTypes.WRITE_TYPE_DEFAULT),
                            descriptors = emptyList(),
                            canRead = false,
                            canWrite = false,
                            readBytes = null,
                            notificationBytes = null
                        )
                    )
                )
            )
        )
    )
}
/*
class ScannedDevicePreviewParameterProvider : PreviewParameterProvider<ScannedDevice> {
    override val values = sequenceOf(
        ScannedDevice(
            0, "ELK-BLEDOM", "24:A9:30:53:5A:97", -45,
            "Microsoft", listOf("Human Readable Device"),
            listOf("Windows 10 Desktop"), 0L,
            customName = null,
            baseRssi = 0, favorite = false, forget = false
        )
    )
}*/
