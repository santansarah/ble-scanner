package com.santansarah.blescanner.utils

import timber.log.Timber


const val UUID_DEFAULT = "-0000-1000-8000-00805F9B34FB"

sealed class ParsableCharacteristic(val uuid: String) {
    object Appearance: ParsableCharacteristic("00002A01$UUID_DEFAULT".lowercase()) {

        fun getCategories(bytes: ByteArray): String {
            val bitSet = bytes.bits()
            val cat = bitSet.substring(0, 10).toInt(2).toHex()
            val subcat = bitSet.substring(10,bitSet.length).toInt( 2).toHex()

            Timber.d(bytes.bits())
            Timber.d(bitSet.substring(0, 10))
            Timber.d(bitSet.substring(10,bitSet.length))

            return "$cat $subcat"

        }

    }
}
