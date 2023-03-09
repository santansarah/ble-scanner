package com.santansarah.blescanner.domain.interfaces

import com.santansarah.blescanner.utils.logging.CharacteristicEvent

interface IAnalytics {

    fun logCharacteristicEvent(analyticsEvent: CharacteristicEvent)
    //fun logScreenEvent(analyticsEvent: CharacteristicEvent)

}