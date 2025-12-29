package com.example.locationalarm.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddAlarm : Screen("add_alarm")
    object EditAlarm : Screen("edit_alarm/{alarmId}") {
        fun createRoute(alarmId: Long) = "edit_alarm/$alarmId"
    }

    companion object {
        const val ALARM_ID_ARG = "alarmId"
    }
}
