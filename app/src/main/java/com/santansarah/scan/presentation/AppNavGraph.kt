package com.santansarah.scan.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.santansarah.scan.domain.models.AppDestinations.CONTROL
import com.santansarah.scan.domain.models.AppDestinations.HOME
import com.santansarah.scan.domain.models.AppRoutes.CONTROL_SCREEN
import com.santansarah.scan.domain.models.AppRoutes.HELP_ABOUT
import com.santansarah.scan.presentation.control.ControlScreen
import com.santansarah.scan.presentation.help.AboutScreen
import com.santansarah.scan.presentation.scan.HomeRoute
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo

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
                appLayoutInfo = appLayoutInfo,
                onHelpClicked = {navController.navigate(HELP_ABOUT)}
            )
        }
        composable(CONTROL) {
            ControlScreen(
                appLayoutInfo = appLayoutInfo,
                onBackClicked = {navController.popBackStack()}
            )
        }
        composable(HELP_ABOUT) {
            AboutScreen(
                appLayoutInfo = appLayoutInfo,
                onBackClicked = {navController.popBackStack()}
            )
        }
    }
}
