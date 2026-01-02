package com.example.locationalarm.presentation.home

import com.example.locationalarm.domain.model.Alarm

data class HomeUiState(
    val alarms: List<Alarm> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val locationCheckResult: LocationCheckResult? = null,
    val deleteConfirmationAlarm: Alarm? = null
)

sealed class LocationCheckResult {
    data class CanEdit(val alarmId: Long) : LocationCheckResult()
    data class CannotAction(val distanceMeters: Int, val action: AlarmAction) : LocationCheckResult()
}
