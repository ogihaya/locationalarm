package com.example.locationalarm.data.repository

import com.example.locationalarm.data.local.AlarmDao
import com.example.locationalarm.data.local.AlarmEntity
import com.example.locationalarm.domain.model.Alarm
import com.example.locationalarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmRepository {

    override fun getAllAlarms(): Flow<List<Alarm>> {
        return alarmDao.getAllAlarms().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAlarmById(id: Long): Alarm? {
        return alarmDao.getAlarmById(id)?.toDomain()
    }

    override suspend fun getEnabledAlarms(): List<Alarm> {
        return alarmDao.getEnabledAlarms().map { it.toDomain() }
    }

    override suspend fun insertAlarm(alarm: Alarm): Long {
        return alarmDao.insertAlarm(alarm.toEntity())
    }

    override suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm.toEntity())
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm.toEntity())
    }

    override suspend fun deleteAlarmById(id: Long) {
        alarmDao.deleteAlarmById(id)
    }

    private fun AlarmEntity.toDomain(): Alarm {
        return Alarm(
            id = id,
            name = name,
            timeInMillis = timeInMillis,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isEnabled = isEnabled
        )
    }

    private fun Alarm.toEntity(): AlarmEntity {
        return AlarmEntity(
            id = id,
            name = name,
            timeInMillis = timeInMillis,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isEnabled = isEnabled
        )
    }
}
