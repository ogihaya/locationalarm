package com.example.locationalarm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.locationalarm.data.local.AlarmDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        // Reschedule all enabled alarms after device boot
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = androidx.room.Room.databaseBuilder(
                    context,
                    AlarmDatabase::class.java,
                    "alarm_database"
                ).build()

                val alarms = database.alarmDao().getEnabledAlarms()
                val scheduler = AlarmScheduler(context)

                alarms.forEach { entity ->
                    if (entity.timeInMillis > System.currentTimeMillis()) {
                        scheduler.scheduleAlarm(
                            com.example.locationalarm.domain.model.Alarm(
                                id = entity.id,
                                name = entity.name,
                                timeInMillis = entity.timeInMillis,
                                latitude = entity.latitude,
                                longitude = entity.longitude,
                                radius = entity.radius,
                                isEnabled = entity.isEnabled
                            )
                        )
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
