package com.santansarah.blescanner.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.santansarah.blescanner.domain.models.AppDestinations.CONTROL
import com.santansarah.blescanner.domain.models.AppDestinations.HOME
import com.santansarah.blescanner.domain.models.AppRoutes.CONTROL_SCREEN
import com.santansarah.blescanner.presentation.control.ControlScreen
import com.santansarah.blescanner.presentation.scan.HomeRoute
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    appLayoutInfo: AppLayoutInfo,
    openDrawer: () -> Unit = {},
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(HOME) {
            HomeRoute(
                onControlClick = { deviceAddress ->
                    navController.navigate("$CONTROL_SCREEN/$deviceAddress")
                },
                appLayoutInfo = appLayoutInfo
            )
        }
        composable(CONTROL) {
            ControlScreen(
                appLayoutInfo = appLayoutInfo,
                onBackClicked = {navController.popBackStack()}
            )
        }
    }
}
