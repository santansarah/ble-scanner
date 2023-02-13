package com.santansarah.blescanner.domain.bleparsables

import com.santansarah.blescanner.domain.models.UUID_DEFAULT

object PreferredConnectionParams : ParsableUuid("00002A04$UUID_DEFAULT".lowercase()) {

    override fun commands(param: Any?): Array<String> {
        return emptyArray()
    }

    override fun getReadStringFromBytes(byteArray: ByteArray): String {

        val sb = StringBuilder()
        with(byteArray) {
            check(size % 2 == 0) { "Must have an even length" }

            //val final = (bytes[7].toUByte().toInt() shl 8) or bytes[7].toUByte().toInt()
            //convert seconds to ms for intervals

            for (i in indices step 2) {
                val twoByteArray = this.copyOfRange(i, i + 2)
                val bitSet = java.util.BitSet.valueOf(twoByteArray)
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


