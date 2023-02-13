package com.santansarah.blescanner.domain.models

import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.utils.BlePermissions
import com.santansarah.blescanner.utils.BleProperties
import com.santansarah.blescanner.utils.BleWriteTypes
import com.santansarah.blescanner.utils.ParsableUuid
import com.santansarah.blescanner.utils.bits
import com.santansarah.blescanner.utils.bitsToHex
import com.santansarah.blescanner.utils.decodeSkipUnreadable
import com.santansarah.blescanner.utils.print
import com.santansarah.blescanner.utils.toBinaryString
import com.santansarah.blescanner.utils.toHex
import timber.log.Timber
import java.lang.StringBuilder

data class DeviceDetail(
    val scannedDevice: ScannedDevice,
    val services: List<DeviceService>
)

data class DeviceService(
    val uuid: String,
    val name: String,
    val characteristics: List<DeviceCharacteristics>
)

data class DeviceCharacteristics(
    val uuid: String,
    val name: String,
    val descriptor: String?,
    val permissions: Int,
    val properties: List<BleProperties>,
    val writeTypes: List<BleWriteTypes>,
    val descriptors: List<DeviceDescriptor>,
    val canRead: Boolean,
    val canWrite: Boolean,
    val readBytes: ByteArray?,
    val notificationBytes: ByteArray?
) {

    fun hasNotify(): Boolean = properties.contains(BleProperties.PROPERTY_NOTIFY)

    fun updateBytes(fromDevice: ByteArray): DeviceCharacteristics {
        Timber.d("fromDevice: $fromDevice")
        return copy(readBytes = fromDevice)
    }

    fun updateDescriptors(uuidFromDevice: String, fromDevice: ByteArray): List<DeviceDescriptor> {
        return descriptors.map {
            if (it.uuid == uuidFromDevice)
                it.copy(readBytes = fromDevice)
            else
                it
        }
    }

    fun updateNotification(fromDevice: ByteArray): DeviceCharacteristics {
        Timber.d("fromNotify: $fromDevice")
        return copy(notificationBytes = fromDevice)
    }

    fun getReadInfo(): String {

        val sb = StringBuilder()

        Timber.d("readbytes from first load: $readBytes")

        readBytes?.let { bytes ->
            with(sb) {
                when (uuid) {
                    ParsableUuid.Appearance.uuid -> {
                        appendLine("Bits, Categories, Value:")
                        appendLine(bytes.bits())
                        appendLine(ParsableUuid.Appearance.getCategories(bytes))
                        appendLine(bytes.bitsToHex())
                    }

                    ParsableUuid.PreferredConnectionParams.uuid ->
                       append(ParsableUuid.PreferredConnectionParams.getConnectionPrefs(bytes))

                    else -> {
                        appendLine("String, Hex, Bytes, Binary:")
                        appendLine(bytes.decodeSkipUnreadable())
                        appendLine(bytes.toHex())
                        appendLine("[" + bytes.print() + "]")
                        appendLine(bytes.toBinaryString())
                    }
                }
            }
        } ?: sb.appendLine("No data.")

        return sb.toString()
    }

    fun getWriteInfo(): Array<String> {
        return when (uuid) {
            ParsableUuid.ELKBLEDOM.uuid -> {
                ParsableUuid.ELKBLEDOM.commands
            }

            else -> {
                emptyArray()
            }
        }
    }

}

data class DeviceDescriptor(
    val uuid: String,
    val name: String,
    val charUuid: String,
    val permissions: List<BlePermissions>,
    val readBytes: ByteArray?
) {

    fun getWriteInfo(): Array<String> {
        return when (uuid) {
            ParsableUuid.CCCD.uuid -> {
                ParsableUuid.CCCD.commands
            }

            else -> {
                emptyArray()
            }
        }
    }

    fun getReadInfo(): String {

        val sb = StringBuilder()

        Timber.d("readbytes from first load: $readBytes")

        readBytes?.let { bytes ->
            with(sb) {
                when (uuid) {
                    ParsableUuid.CCCD.uuid -> {
                        appendLine(ParsableUuid.CCCD.notificationsEnabled(bytes))
                        appendLine("[" + bytes.print() + "]")
                    }

                    else -> {
                        appendLine("String, Hex, Bytes, Binary:")
                        appendLine(bytes.decodeSkipUnreadable())
                        appendLine(bytes.toHex())
                        appendLine("[" + bytes.print() + "]")
                        appendLine(bytes.toBinaryString())
                    }
                }
            }
        } ?: sb.appendLine("No data.")

        return sb.toString()
    }

    fun updateBytes(fromDevice: ByteArray): DeviceDescriptor {
        Timber.d("fromDevice: $fromDevice")
        return copy(readBytes = fromDevice)
    }

}


