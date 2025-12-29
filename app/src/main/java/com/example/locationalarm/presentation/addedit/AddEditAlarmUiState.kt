package com.example.locationalarm.presentation.addedit

import com.google.android.gms.maps.model.LatLng

data class AddEditAlarmUiState(
    val alarmId: Long? = null,
    val name: String = "",
    val timeHour: Int = 7,
    val timeMinute: Int = 0,
    val destination: LatLng? = null,
    val radius: Int = 100,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val isEditMode: Boolean = false
)
