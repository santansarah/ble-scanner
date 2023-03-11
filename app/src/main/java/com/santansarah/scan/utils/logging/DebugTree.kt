package com.santansarah.scan.utils.logging

import timber.log.Timber

class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        super.createStackElementTag(element)
        return "${element.fileName}:${element.lineNumber}:${element.methodName}: "
    }

}