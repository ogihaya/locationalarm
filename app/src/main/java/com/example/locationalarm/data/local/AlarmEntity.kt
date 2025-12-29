package com.example.locationalarm.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val timeInMillis: Long,
    val latitude: Double,
    val longitude: Double,
    val radius: Int = 100, // meters
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
