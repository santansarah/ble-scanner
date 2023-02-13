package com.santansarah.blescanner.domain.bleparsables

import com.santansarah.blescanner.domain.models.UUID_DEFAULT
import com.santansarah.blescanner.utils.toHex

object ELKBLEDOM : ParsableUuid("0000FFF3$UUID_DEFAULT".lowercase()) {

    const val on = "7e0004f00001ff00ef"
    const val off = "7e0004000000ff00ef"

    fun isOn(bytes: ByteArray): Boolean {
        val firstBytes = bytes.copyOfRange(0, 8)
        return firstBytes.toHex() != off
    }

    fun getLedStatus(bytes: ByteArray): String {
        val firstBytes = bytes.copyOfRange(0, 8)
        val nextBytes = bytes.copyOfRange(9, 14)
        val finalByes = bytes.copyOfRange(15, 24)

        return firstBytes.toHex()

    }

    override fun commands(param: Any?): Array<String> {
        return arrayOf(
            "On: 7e0004f00001ff00ef",
            "Off: 7e0004000000ff00ef",
            "Color: 7e000503xxxxxx00ef"
        )
    }

    override fun getReadStringFromBytes(byteArray: ByteArray): String {
        TODO("Not yet implemented")
    }

}

