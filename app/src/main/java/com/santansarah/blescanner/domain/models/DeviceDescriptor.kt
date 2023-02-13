package com.santansarah.blescanner.domain.models

import com.santansarah.blescanner.domain.bleparsables.CCCD
import com.santansarah.blescanner.utils.decodeSkipUnreadable
import com.santansarah.blescanner.utils.print
import com.santansarah.blescanner.utils.toBinaryString
import com.santansarah.blescanner.utils.toHex
import timber.log.Timber

data class DeviceDescriptor(
    val uuid: String,
    val name: String,
    val charUuid: String,
    val permissions: List<BlePermissions>,
    val notificationProperty: BleProperties?,
    val readBytes: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceDescriptor

        if (uuid != other.uuid) return false
        if (name != other.name) return false
        if (charUuid != other.charUuid) return false
        if (permissions != other.permissions) return false
        if (notificationProperty != other.notificationProperty) return false
        if (readBytes != null) {
            if (other.readBytes == null) return false
            if (!readBytes.contentEquals(other.readBytes)) return false
        } else if (other.readBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + charUuid.hashCode()
        result = 31 * result + permissions.hashCode()
        result = 31 * result + (notificationProperty?.hashCode() ?: 0)
        result = 31 * result + (readBytes?.contentHashCode() ?: 0)
        return result
    }
}


fun DeviceDescriptor.getWriteCommands(): Array<String> {
    return when (uuid) {
        CCCD.uuid -> {
            CCCD.commands(notificationProperty!!)
        }

        else -> {
            emptyArray()
        }
    }
}

fun DeviceDescriptor.getReadInfo(): String {

    val sb = StringBuilder()

    Timber.d("readbytes from first load: $readBytes")

    readBytes?.let { bytes ->
        with(sb) {
            when (uuid) {
                CCCD.uuid -> {
                    appendLine(CCCD.getReadStringFromBytes(bytes))
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



