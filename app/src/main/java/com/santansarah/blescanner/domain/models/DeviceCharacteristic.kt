package com.santansarah.blescanner.domain.models


import com.santansarah.blescanner.domain.bleparsables.Appearance
import com.santansarah.blescanner.domain.bleparsables.ELKBLEDOM
import com.santansarah.blescanner.domain.bleparsables.PreferredConnectionParams
import com.santansarah.blescanner.utils.bits
import com.santansarah.blescanner.utils.bitsToHex
import com.santansarah.blescanner.utils.decodeSkipUnreadable
import com.santansarah.blescanner.utils.print
import com.santansarah.blescanner.utils.toBinaryString
import com.santansarah.blescanner.utils.toHex
import timber.log.Timber

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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceCharacteristics

        if (uuid != other.uuid) return false
        if (name != other.name) return false
        if (descriptor != other.descriptor) return false
        if (permissions != other.permissions) return false
        if (properties != other.properties) return false
        if (writeTypes != other.writeTypes) return false
        if (descriptors != other.descriptors) return false
        if (canRead != other.canRead) return false
        if (canWrite != other.canWrite) return false
        if (readBytes != null) {
            if (other.readBytes == null) return false
            if (!readBytes.contentEquals(other.readBytes)) return false
        } else if (other.readBytes != null) return false
        if (notificationBytes != null) {
            if (other.notificationBytes == null) return false
            if (!notificationBytes.contentEquals(other.notificationBytes)) return false
        } else if (other.notificationBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (descriptor?.hashCode() ?: 0)
        result = 31 * result + permissions
        result = 31 * result + properties.hashCode()
        result = 31 * result + writeTypes.hashCode()
        result = 31 * result + descriptors.hashCode()
        result = 31 * result + canRead.hashCode()
        result = 31 * result + canWrite.hashCode()
        result = 31 * result + (readBytes?.contentHashCode() ?: 0)
        result = 31 * result + (notificationBytes?.contentHashCode() ?: 0)
        return result
    }
}

fun DeviceCharacteristics.hasNotify(): Boolean = properties.contains(BleProperties.PROPERTY_NOTIFY)
fun DeviceCharacteristics.hasIndicate(): Boolean = properties.contains(BleProperties.PROPERTY_INDICATE)

fun DeviceCharacteristics.updateBytes(fromDevice: ByteArray): DeviceCharacteristics {
    Timber.d("fromDevice: $fromDevice")
    return copy(readBytes = fromDevice)
}

fun DeviceCharacteristics.updateDescriptors(uuidFromDevice: String, fromDevice: ByteArray): List<DeviceDescriptor> {
    return descriptors.map {
        if (it.uuid == uuidFromDevice)
            it.copy(readBytes = fromDevice)
        else
            it
    }
}

fun DeviceCharacteristics.updateNotification(fromDevice: ByteArray): DeviceCharacteristics {
    Timber.d("fromNotify: $fromDevice")
    //return copy(notificationBytes = fromDevice)
    return copy(readBytes = fromDevice)
}

fun DeviceCharacteristics.getReadInfo(): String {

    val sb = StringBuilder()

    Timber.d("readbytes from first load: $readBytes")

    readBytes?.let { bytes ->
        with(sb) {
            when (uuid) {
                Appearance.uuid -> {
                    appendLine(Appearance.getReadStringFromBytes(bytes))
                }

                PreferredConnectionParams.uuid ->
                    append(PreferredConnectionParams.getReadStringFromBytes(bytes))

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

fun DeviceCharacteristics.getWriteCommands(): Array<String> {
    return when (uuid) {
        ELKBLEDOM.uuid -> {
            ELKBLEDOM.commands()
        }

        else -> {
            emptyArray()
        }
    }
}
