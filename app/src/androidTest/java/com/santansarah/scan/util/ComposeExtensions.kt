package com.santansarah.scan.util

import com.santansarah.scan.utils.AsyncTimer
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