package com.santansarah.blescanner.domain.models

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.presentation.theme.codeFont
import com.santansarah.blescanner.utils.ParsableCharacteristic
import com.santansarah.blescanner.utils.bits
import com.santansarah.blescanner.utils.bitsToHex
import com.santansarah.blescanner.utils.decodeSkipUnreadable
import com.santansarah.blescanner.utils.print
import com.santansarah.blescanner.utils.toBinaryString
import com.santansarah.blescanner.utils.toHex
import timber.log.Timber
import java.lang.StringBuilder

enum class ConnectionState {
    CONNECTED,
    CONNECTING,
    DISCONNECTING,
    DISCONNECTED;

    fun toTitle() = this.name.lowercase().replaceFirstChar { it.uppercase() }
}

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
    val properties: Int,
    val writeTypes: Int,
    val descriptors: List<DeviceDescriptor>,
    val canRead: Boolean,
    val canWrite: Boolean,
    val readBytes: ByteArray?,
) {
    fun updateBytes(fromDevice: ByteArray): DeviceCharacteristics {
        Timber.d("fromDevice: $fromDevice")
        return copy(readBytes = fromDevice)
    }

    fun getReadInfo(): String {

        val sb = StringBuilder()

        Timber.d("readbytes from first load: $readBytes")

        readBytes?.let {bytes ->
            with (sb) {
                when (uuid) {
                    ParsableCharacteristic.Appearance.uuid -> {
                        appendLine("Bits, Categories, Value:")
                        appendLine(bytes.bits())
                        appendLine(ParsableCharacteristic.Appearance.getCategories(bytes))
                        appendLine(bytes.bitsToHex())
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

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (descriptor?.hashCode() ?: 0)
        result = 31 * result + permissions
        result = 31 * result + properties
        result = 31 * result + writeTypes
        result = 31 * result + descriptors.hashCode()
        result = 31 * result + canRead.hashCode()
        result = 31 * result + canWrite.hashCode()
        result = 31 * result + (readBytes?.contentHashCode() ?: 0)
        return result
    }
}

data class DeviceDescriptor(
    val uuid: String,
    val permissions: Int
)


