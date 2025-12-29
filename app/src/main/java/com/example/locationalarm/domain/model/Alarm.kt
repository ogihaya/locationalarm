package com.example.locationalarm.domain.model

data class Alarm(
    val id: Long = 0,
    val name: String,
    val timeInMillis: Long,
    val latitude: Double,
    val longitude: Double,
    val radius: Int = 100,
    val isEnabled: Boolean = true
) {
    val displayName: String
        get() = name.ifBlank { "アラーム" }
}
