package com.example.locationalarm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.locationalarm.presentation.addedit.AddEditAlarmScreen
import com.example.locationalarm.presentation.home.HomeScreen

@Composable
fun AlarmNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddAlarm = {
                    navController.navigate(Screen.AddAlarm.route)
                },
                onNavigateToEditAlarm = { alarmId ->
                    navController.navigate(Screen.EditAlarm.createRoute(alarmId))
                }
            )
        }

        composable(Screen.AddAlarm.route) {
            AddEditAlarmScreen(
                alarmId = null,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditAlarm.route,
            arguments = listOf(
                navArgument(Screen.ALARM_ID_ARG) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val alarmId = backStackEntry.arguments?.getLong(Screen.ALARM_ID_ARG)
            AddEditAlarmScreen(
                alarmId = alarmId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
