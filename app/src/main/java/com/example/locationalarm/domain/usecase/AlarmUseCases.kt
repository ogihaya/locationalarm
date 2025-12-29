package com.example.locationalarm.domain.usecase

import com.example.locationalarm.domain.model.Alarm
import com.example.locationalarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAlarmsUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    operator fun invoke(): Flow<List<Alarm>> = repository.getAllAlarms()
}

class GetAlarmByIdUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Long): Alarm? = repository.getAlarmById(id)
}

class AddAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm): Long = repository.insertAlarm(alarm)
}

class UpdateAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm) = repository.updateAlarm(alarm)
}

class DeleteAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm) = repository.deleteAlarm(alarm)
}

class DeleteAlarmByIdUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteAlarmById(id)
}
