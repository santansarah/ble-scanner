package com.santansarah.scan.utils.logging

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.santansarah.scan.domain.interfaces.IAnalytics
import org.koin.core.component.KoinComponent


enum class AnalyticsEventType {
    READ_CHARACTERISTIC,
    WRITE_CHARACTERISTIC,
    CONTROL_ELKBLEDOM
}

enum class AnalyticsContentType {
    BLE_CHARACTERISTIC,
    CONTROL_EVENT
}

data class CharacteristicEvent(
    val eventName: String,
    val uuid: String
)

data class ScreenEvent(
    val eventName: String,
    val uuid: String
)

class Analytics(private val firebaseAnalytics: FirebaseAnalytics) :
    KoinComponent, IAnalytics {

    override fun logCharacteristicEvent(analyticsEvent: CharacteristicEvent) {

        /*val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, analyticsEvent.eventType.name)
        params.putString(FirebaseAnalytics.Param.ITEM_ID, analyticsEvent.contentValue)

        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SELECT_ITEM,
            params
        )*/

        val params = Bundle()
        params.putString("uuid", analyticsEvent.uuid)

        firebaseAnalytics.logEvent(
            analyticsEvent.eventName,
            params)

    }


}