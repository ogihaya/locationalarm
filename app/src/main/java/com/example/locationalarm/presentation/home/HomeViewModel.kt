package com.example.locationalarm.presentation.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationalarm.alarm.AlarmScheduler
import com.example.locationalarm.domain.model.Alarm
import com.example.locationalarm.domain.usecase.CalculateDistanceUseCase
import com.example.locationalarm.domain.usecase.DeleteAlarmByIdUseCase
import com.example.locationalarm.domain.usecase.GetAllAlarmsUseCase
import com.example.locationalarm.domain.usecase.UpdateAlarmUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllAlarmsUseCase: GetAllAlarmsUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val deleteAlarmByIdUseCase: DeleteAlarmByIdUseCase,
    private val calculateDistanceUseCase: CalculateDistanceUseCase,
    private val alarmScheduler: AlarmScheduler,
    private val fusedLocationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadAlarms()
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            getAllAlarmsUseCase().collect { alarms ->
                _uiState.update {
                    it.copy(alarms = alarms, isLoading = false)
                }
            }
        }
    }

    fun toggleAlarmEnabled(alarm: Alarm) {
        viewModelScope.launch {
            checkLocationAndPerformAction(alarm, AlarmAction.Toggle(alarm))
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            checkLocationAndPerformAction(alarm, AlarmAction.Delete(alarm))
        }
    }

    fun checkLocationForEdit(alarm: Alarm) {
        viewModelScope.launch {
            checkLocationAndPerformAction(alarm, AlarmAction.Edit(alarm.id))
        }
    }

    private suspend fun checkLocationAndPerformAction(alarm: Alarm, action: AlarmAction) {
        try {
            val location = getCurrentLocation()
            if (location != null) {
                val isWithin = calculateDistanceUseCase.isWithinRadius(
                    location.latitude,
                    location.longitude,
                    alarm.latitude,
                    alarm.longitude,
                    alarm.radius
                )

                if (isWithin) {
                    when (action) {
                        is AlarmAction.Edit -> {
                            _uiState.update {
                                it.copy(locationCheckResult = LocationCheckResult.CanEdit(action.alarmId))
                            }
                        }
                        is AlarmAction.Toggle -> {
                            performToggle(action.alarm)
                        }
                        is AlarmAction.Delete -> {
                            // Instead of deleting immediately, show confirmation
                            _uiState.update {
                                it.copy(deleteConfirmationAlarm = action.alarm)
                            }
                        }
                    }
                } else {
                    val distance = calculateDistanceUseCase(
                        location.latitude,
                        location.longitude,
                        alarm.latitude,
                        alarm.longitude
                    )
                    _uiState.update {
                        it.copy(
                            locationCheckResult = LocationCheckResult.CannotAction(
                                distance.toInt() - alarm.radius,
                                action
                            )
                        )
                    }
                }
            } else {
                _uiState.update {
                    it.copy(errorMessage = "位置情報を取得できませんでした")
                }
            }
        } catch (e: SecurityException) {
            _uiState.update {
                it.copy(errorMessage = "位置情報の権限がありません")
            }
        }
    }

    private suspend fun performToggle(alarm: Alarm) {
        val updatedAlarm = alarm.copy(isEnabled = !alarm.isEnabled)
        updateAlarmUseCase(updatedAlarm)

        if (updatedAlarm.isEnabled) {
            alarmScheduler.scheduleAlarm(updatedAlarm)
        } else {
            alarmScheduler.cancelAlarm(updatedAlarm.id)
        }
        
        loadAlarms()
    }

    // Called when user confirms deletion in the dialog
    fun deleteAlarmConfirmed(alarmId: Long) {
        viewModelScope.launch {
            deleteAlarmByIdUseCase(alarmId)
            alarmScheduler.cancelAlarm(alarmId)
            // Clear the dialog state
            _uiState.update { it.copy(deleteConfirmationAlarm = null) }
            
            // Reload alarms is implicit via flow collection, but just in case
        }
    }
    
    fun cancelDelete() {
        _uiState.update { it.copy(deleteConfirmationAlarm = null) }
    }

    fun clearLocationCheckResult() {
        _uiState.update { it.copy(locationCheckResult = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private suspend fun getCurrentLocation(): Location? {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        return try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()
        } catch (e: Exception) {
            null
        }
    }
}

sealed interface AlarmAction {
    data class Edit(val alarmId: Long) : AlarmAction
    data class Toggle(val alarm: Alarm) : AlarmAction
    data class Delete(val alarm: Alarm) : AlarmAction
}
