package com.santansarah.blescanner.domain.bleparsables

abstract class ParsableUuid(val uuid: String) {

    abstract fun commands(param: Any? = null): Array<String>
    abstract fun getReadStringFromBytes(byteArray: ByteArray): String

}
