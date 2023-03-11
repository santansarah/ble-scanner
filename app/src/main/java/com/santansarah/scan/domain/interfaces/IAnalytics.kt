package com.santansarah.scan.domain.interfaces

import com.santansarah.scan.utils.logging.CharacteristicEvent

interface IAnalytics {

    fun logCharacteristicEvent(analyticsEvent: CharacteristicEvent)
    //fun logScreenEvent(analyticsEvent: CharacteristicEvent)

}