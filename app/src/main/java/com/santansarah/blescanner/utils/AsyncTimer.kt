package com.santansarah.blescanner.utils

import java.util.Timer
import kotlin.concurrent.schedule

object AsyncTimer {
    var expired = false
    fun start(delay: Long = 1000) {
        expired = false
        Timer().schedule(delay) {
            expired = true
        }
    }
}