package com.santansarah.blescanner.domain.bleparsables

import com.santansarah.blescanner.domain.models.UUID_DEFAULT
import com.santansarah.blescanner.utils.bits
import com.santansarah.blescanner.utils.toHex
import timber.log.Timber

object Appearance : ParsableUuid("00002A01$UUID_DEFAULT".lowercase()) {

    override fun commands(param: Any?): Array<String> {
        return emptyArray()
    }

    override fun getReadStringFromBytes(byteArray: ByteArray): String {
        val bitSet = byteArray.bits()
        val cat = bitSet.substring(0, 10).toInt(2).toHex()
        val subcat = bitSet.substring(10, bitSet.length).toInt(2).toHex()

        Timber.d(byteArray.bits())
        Timber.d(bitSet.substring(0, 10))
        Timber.d(bitSet.substring(10, bitSet.length))

        return "$cat $subcat"

    }

}
