package com.santansarah.blescanner.utils.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        super.createStackElementTag(element)
        return "${element.fileName}:${element.lineNumber}:${element.methodName}: "
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)

        FirebaseCrashlytics.getInstance().also {
            it.setCustomKey(ReleaseTree.Priority, priority)
            tag?.let { _ -> it.setCustomKey(ReleaseTree.Tag, tag) }
            it.log(message)
            t?.let { e -> it.recordException(e) }
        }
    }

}