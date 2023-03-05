package com.santansarah.blescanner.util

import com.santansarah.blescanner.utils.AsyncTimer
import de.mannodermaus.junit5.compose.ComposeContext

fun ComposeContext.waitUntilTimeout(
    timeoutMillis: Long
) {
    AsyncTimer.start(timeoutMillis)
    this.waitUntil(
        condition = { AsyncTimer.expired },
        timeoutMillis = timeoutMillis + 1000
    )
}