package com.example.locationalarm.alarm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.example.locationalarm.LocationAlarmApp
import com.example.locationalarm.R
import com.example.locationalarm.presentation.ringing.AlarmRingingActivity

class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private val handler = Handler(Looper.getMainLooper())
    private var repeatCount = 0
    private val maxRepeatCount = 60 // Maximum 10 minutes (60 * 10 seconds)

    private val repeatRunnable = object : Runnable {
        override fun run() {
            if (repeatCount < maxRepeatCount) {
                playAlarmSound()
                vibrateDevice()
                repeatCount++
                handler.postDelayed(this, 10000) // Repeat every 10 seconds
            } else {
                stopSelf()
            }
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1001
        const val ACTION_STOP_ALARM = "com.example.locationalarm.STOP_ALARM"

        var currentAlarmId: Long = -1
            private set
        var currentLatitude: Double = 0.0
            private set
        var currentLongitude: Double = 0.0
            private set
        var currentRadius: Int = 100
            private set
        var currentAlarmName: String = "アラーム"
            private set
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_ALARM) {
            stopAlarm()
            return START_NOT_STICKY
        }

        val alarmId = intent?.getLongExtra(AlarmReceiver.EXTRA_ALARM_ID, -1) ?: -1
        val alarmName = intent?.getStringExtra(AlarmReceiver.EXTRA_ALARM_NAME) ?: "アラーム"
        val latitude = intent?.getDoubleExtra(AlarmReceiver.EXTRA_LATITUDE, 0.0) ?: 0.0
        val longitude = intent?.getDoubleExtra(AlarmReceiver.EXTRA_LONGITUDE, 0.0) ?: 0.0
        val radius = intent?.getIntExtra(AlarmReceiver.EXTRA_RADIUS, 100) ?: 100

        if (alarmId == -1L) {
            stopSelf()
            return START_NOT_STICKY
        }

        currentAlarmId = alarmId
        currentAlarmName = alarmName
        currentLatitude = latitude
        currentLongitude = longitude
        currentRadius = radius

        // Start foreground with notification
        startForeground(NOTIFICATION_ID, createNotification(alarmName))

        // Start alarm sound and vibration loop
        handler.post(repeatRunnable)

        return START_STICKY
    }

    private fun createNotification(alarmName: String): Notification {
        val fullScreenIntent = Intent(this, AlarmRingingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            this,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_STOP_ALARM
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, LocationAlarmApp.ALARM_CHANNEL_ID)
            .setContentTitle(alarmName)
            .setContentText(getString(R.string.tap_to_open))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                getString(R.string.stop_alarm),
                stopPendingIntent
            )
            .build()
    }

    private fun playAlarmSound() {
        try {
            // Stop previous playback
            mediaPlayer?.release()

            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@AlarmService, alarmUri)

                // Set audio attributes to bypass silent mode
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )

                // Set volume to max for alarm stream
                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
                audioManager.setStreamVolume(
                    AudioManager.STREAM_ALARM,
                    maxVolume,
                    0
                )

                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun vibrateDevice() {
        try {
            val pattern = longArrayOf(0, 500, 200, 500, 200, 500)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopAlarm() {
        handler.removeCallbacks(repeatRunnable)
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator?.cancel()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        handler.removeCallbacks(repeatRunnable)
        mediaPlayer?.release()
        vibrator?.cancel()
        super.onDestroy()
    }
}
