package com.santansarah.blescanner.utils

import timber.log.Timber


const val UUID_DEFAULT = "-0000-1000-8000-00805F9B34FB"

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
}
