package com.santansarah.blescanner.domain.bleparsables

import com.santansarah.blescanner.domain.models.UUID_DEFAULT
import com.santansarah.blescanner.utils.toHex
import timber.log.Timber

object ELKBLEDOM : ParsableUuid("0000FFF3$UUID_DEFAULT".lowercase()) {

    const val ON = "0x7E0004F00001FF00EF"
    const val OFF = "0x7E0004000000FF00EF"
    const val COLOR = "0x7E000503xxxxxx00EF"
    const val BRIGHTNESS = "0x7E0001xx00000000EF"

/*
         0x7E 00 04 00 00 00 FF 00 EF
    off: 0x7E 04 04 00 00 00 FF 00 EF
    0x7E 05 03 80 03FFFF 00EF
    0x7E 00 05 03 FF0000 00EF
    0x7E 07 05 03 00FF00 10EF
*/

    val white = "FFFFFF"
    val yellow = "FFFF00"
    val cyan = "00FFFF"
    val green = "00FF00"
    val magenta = "FF00FF"
    val blue = "0000FF"
    val orange = "FFA500"
    val red = "FF0000"

    val colorsHex = arrayOf(white, yellow, cyan, green, magenta, blue, orange, red)

    fun onOffState(bytes: ByteArray): Boolean {
        val onOffByte = bytes[2]
        val onOffParam = bytes[5]

        Timber.d(bytes.toHex())

        return !(onOffByte.toInt() == 4 && onOffParam.toInt() == 0)
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

