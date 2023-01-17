package com.santansarah.blescanner.utils

import java.util.UUID

/**
 * Use “%02x” to convert the given byte to its corresponding hex value.
 * Moreover, it pads the hex value with a leading zero if necessary.
 */
fun ByteArray.toHex(): String =
    joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

fun Byte.toHex(): String = "%02x".format(this)

// "^([A-F0-9]{4}|[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12})$"
// (4 or 8 in beginning) yyyyxxxx-0000-1000-8000-00805f9b34fb
fun UUID.toGss() =
    this.toString()
        .replaceFirst(Regex("^0+(?!$)"), "")
        .replace("-0000-1000-8000-00805f9b34fb", "")

