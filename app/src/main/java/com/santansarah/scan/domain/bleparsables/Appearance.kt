package com.santansarah.scan.domain.bleparsables

import com.santansarah.scan.domain.models.UUID_DEFAULT
import com.santansarah.scan.utils.bits
import com.santansarah.scan.utils.bitsToHex
import com.santansarah.scan.utils.toHex

object Appearance : ParsableUuid("00002A01$UUID_DEFAULT".lowercase()) {

    override fun commands(param: Any?): Array<String> {
        return emptyArray()
    }

    override fun getReadStringFromBytes(byteArray: ByteArray): String {
        val bitSet = byteArray.bits()
        val cat = bitSet.substring(0, 10).toInt(2).toHex()
        val subcat = bitSet.substring(10, bitSet.length).toInt(2).toHex()

        val sb = StringBuilder()
        with (sb) {
            appendLine("Bits, Categories, Value:")
            appendLine(bitSet)
            appendLine("$cat $subcat")
            appendLine(byteArray.bitsToHex())
        }

        return sb.toString()

    }

}
