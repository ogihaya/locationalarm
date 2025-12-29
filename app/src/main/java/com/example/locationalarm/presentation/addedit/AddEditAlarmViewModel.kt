package com.example.locationalarm.presentation.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationalarm.alarm.AlarmScheduler
import com.example.locationalarm.domain.model.Alarm
import com.example.locationalarm.domain.usecase.AddAlarmUseCase
import com.example.locationalarm.domain.usecase.GetAlarmByIdUseCase
import com.example.locationalarm.domain.usecase.UpdateAlarmUseCase
import com.example.locationalarm.presentation.navigation.Screen
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditAlarmViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val alarmId: Long? = savedStateHandle.get<Long>(Screen.ALARM_ID_ARG)

    private val _uiState = MutableStateFlow(AddEditAlarmUiState())
    val uiState: StateFlow<AddEditAlarmUiState> = _uiState.asStateFlow()

    init {
        if (alarmId != null && alarmId > 0) {
            loadAlarm(alarmId)
        } else {
            // Set default time to current time + 1 hour
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            _uiState.update {
                it.copy(
                    timeHour = calendar.get(Calendar.HOUR_OF_DAY),
                    timeMinute = calendar.get(Calendar.MINUTE)
                )
            }
        }
    }

    private fun loadAlarm(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val alarm = getAlarmByIdUseCase(id)
            if (alarm != null) {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = alarm.timeInMillis
                }
                _uiState.update {
                    it.copy(
                        alarmId = alarm.id,
                        name = alarm.name,
                        timeHour = calendar.get(Calendar.HOUR_OF_DAY),
                        timeMinute = calendar.get(Calendar.MINUTE),
                        destination = LatLng(alarm.latitude, alarm.longitude),
                        radius = alarm.radius,
                        isLoading = false,
                        isEditMode = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "アラームが見つかりません")
                }
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(timeHour = hour, timeMinute = minute) }
    }

    fun updateDestination(latLng: LatLng) {
        _uiState.update { it.copy(destination = latLng) }
    }

    fun updateRadius(radius: Int) {
        _uiState.update { it.copy(radius = radius) }
    }

    fun saveAlarm() {
        val state = _uiState.value

        if (state.destination == null) {
            _uiState.update { it.copy(errorMessage = "目的地を選択してください") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, state.timeHour)
                set(Calendar.MINUTE, state.timeMinute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                // If the time is in the past, set it to tomorrow
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            val alarm = Alarm(
                id = state.alarmId ?: 0,
                name = state.name,
                timeInMillis = calendar.timeInMillis,
                latitude = state.destination.latitude,
                longitude = state.destination.longitude,
                radius = state.radius,
                isEnabled = true
            )

            try {
                val savedId = if (state.isEditMode) {
                    updateAlarmUseCase(alarm)
                    alarm.id
                } else {
                    addAlarmUseCase(alarm)
                }

                // Schedule the alarm
                alarmScheduler.scheduleAlarm(alarm.copy(id = savedId))

                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSaving = false, errorMessage = "保存に失敗しました: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
