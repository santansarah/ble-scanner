package com.santansarah.blescanner.utils

import android.bluetooth.BluetoothGattDescriptor
import timber.log.Timber
import java.util.BitSet


const val UUID_DEFAULT = "-0000-1000-8000-00805F9B34FB"

enum class BlePermissions(val value: Int) {
    PERMISSION_READ(1),
    PERMISSION_READ_ENCRYPTED(2),
    PERMISSION_READ_ENCRYPTED_MITM(4),
    PERMISSION_WRITE(16),
    PERMISSION_WRITE_ENCRYPTED(32),
    PERMISSION_WRITE_ENCRYPTED_MITM(64),
    PERMISSION_WRITE_SIGNED(128),
    PERMISSION_WRITE_SIGNED_MITM(256);

    companion object {

        fun getAllPermissions(bleValue: Int): List<BlePermissions> {
            var propertyList = mutableListOf<BlePermissions>()

            values().forEach {
                if (bleValue and it.value > 0)
                    propertyList.add(it)
            }
            return propertyList

        }
    }
}

enum class BleWriteTypes(val value: Int) {
    WRITE_TYPE_DEFAULT(2),
    WRITE_TYPE_NO_RESPONSE(1),
    WRITE_TYPE_SIGNED(4);

    companion object {

        fun getAllTypes(bleValue: Int): List<BleWriteTypes> {
            var propertyList = mutableListOf<BleWriteTypes>()

            values().forEach {
                if (bleValue and it.value > 0)
                    propertyList.add(it)
            }
            return propertyList

        }
    }
}

enum class BleProperties(val value: Int) {
    PROPERTY_BROADCAST(1),
    PROPERTY_EXTENDED_PROPS(128),
    PROPERTY_INDICATE(32),
    PROPERTY_NOTIFY(16),
    PROPERTY_READ(2),
    PROPERTY_SIGNED_WRITE(64),
    PROPERTY_WRITE(8),
    PROPERTY_WRITE_NO_RESPONSE(4);

    companion object {

        fun getAllProperties(bleValue: Int): List<BleProperties> {
            var propertyList = mutableListOf<BleProperties>()

            values().forEach {
                if (bleValue and it.value > 0)
                    propertyList.add(it)
            }
            return propertyList

        }
    }
}

fun List<BleProperties>.canRead(): Boolean = this.contains(BleProperties.PROPERTY_READ)
fun List<BleProperties>.canWrite(): Boolean = this.any(
    listOf(
        BleProperties.PROPERTY_WRITE, BleProperties.PROPERTY_SIGNED_WRITE,
        BleProperties.PROPERTY_WRITE_NO_RESPONSE
    )::contains
)

fun List<BleProperties>.propsToString() =
    this.joinToString(", ") { it.name }

fun List<BleWriteTypes>.writeTypesToString() = this.joinToString(", ") { it.name }

sealed class ParsableCharacteristic(val uuid: String) {

    object ELKBLEDOM : ParsableCharacteristic("0000FFF3$UUID_DEFAULT".lowercase()) {

        const val on = "7e0004f00001ff00ef"
        const val off = "7e0004000000ff00ef"

        fun isOn(bytes: ByteArray): Boolean {
            val firstBytes = bytes.copyOfRange(0, 8)
            return firstBytes.toHex() != off
        }

        val commands = arrayOf(
            "On: 7e0004f00001ff00ef",
            "Off: 7e0004000000ff00ef",
            "Color: 7e000503xxxxxx00ef"
        )

        fun getLedStatus(bytes: ByteArray): String {
            val firstBytes = bytes.copyOfRange(0, 8)
            val nextBytes = bytes.copyOfRange(9, 14)
            val finalByes = bytes.copyOfRange(15, 24)

            return firstBytes.toHex()

        }

    }

    object Appearance : ParsableCharacteristic("00002A01$UUID_DEFAULT".lowercase()) {

        fun getCategories(bytes: ByteArray): String {
            val bitSet = bytes.bits()
            val cat = bitSet.substring(0, 10).toInt(2).toHex()
            val subcat = bitSet.substring(10, bitSet.length).toInt(2).toHex()

            Timber.d(bytes.bits())
            Timber.d(bitSet.substring(0, 10))
            Timber.d(bitSet.substring(10, bitSet.length))

            return "$cat $subcat"

        }

    }

    object CCCD : ParsableCharacteristic("00002902$UUID_DEFAULT".lowercase()) {
        fun notificationsEnabled(bytes: ByteArray): String {
            return if (bytes.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) ||
                bytes.contentEquals(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE)
            )
                "Enabled."
            else
                "Disabled."
        }
    }

    object PreferredConnectionParams : ParsableCharacteristic("00002A04$UUID_DEFAULT".lowercase()) {

        fun getConnectionPrefs(bytes: ByteArray): String {

            val sb = StringBuilder()
            with(bytes) {
                check(size % 2 == 0) { "Must have an even length" }

                //val final = (bytes[7].toUByte().toInt() shl 8) or bytes[7].toUByte().toInt()
                //convert seconds to ms for intervals

                for (i in indices step 2) {
                    val twoByteArray = this.copyOfRange(i, i + 2)
                    val bitSet = BitSet.valueOf(twoByteArray)
                    when (i) {
                        0 -> sb.append(
                            "Connection Interval(ms): ${
                                bitSet.toLongArray().first() * 1.25
                            } - "
                        )

                        2 -> sb.append((bitSet.toLongArray().first() * 1.25).toString() + "\n")
                        4 -> sb.appendLine("Latency: ${bitSet.toLongArray().first()}")
                        6 -> sb.appendLine("Timeout Multiplier: ${bitSet.toLongArray().first()}")
                        else -> {}
                    }
                }
            }
            return sb.toString()
        }

    }

}
