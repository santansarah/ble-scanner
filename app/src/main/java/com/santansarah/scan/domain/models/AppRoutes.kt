package com.santansarah.scan.domain.models

import com.santansarah.scan.domain.models.AppRouteArgs.ADDRESS
import com.santansarah.scan.domain.models.AppRoutes.CONTROL_SCREEN
import com.santansarah.scan.domain.models.AppRoutes.HOME_SCREEN

object AppRoutes {
    const val HOME_SCREEN = "home"
    const val CONTROL_SCREEN = "control"
}

object AppRouteArgs {
    const val ADDRESS = "address"
}

object AppDestinations {
    const val HOME = HOME_SCREEN
    const val CONTROL = "$CONTROL_SCREEN/{$ADDRESS}"
}