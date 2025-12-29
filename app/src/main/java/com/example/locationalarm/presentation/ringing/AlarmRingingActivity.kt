package com.example.locationalarm.presentation.ringing

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.locationalarm.alarm.AlarmService
import com.example.locationalarm.presentation.theme.LocationAlarmTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AlarmRingingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Wake up device and show on lock screen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        setContent {
            LocationAlarmTheme {
                AlarmRingingScreen(
                    context = this,
                    alarmName = AlarmService.currentAlarmName,
                    targetLat = AlarmService.currentLatitude,
                    targetLon = AlarmService.currentLongitude,
                    radius = AlarmService.currentRadius,
                    onStop = {
                        stopAlarmService()
                        finish()
                    }
                )
            }
        }
    }

    private fun stopAlarmService() {
        val intent = Intent(this, AlarmService::class.java).apply {
            action = AlarmService.ACTION_STOP_ALARM
        }
        startService(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Prevent back button from dismissing the alarm
        // User must stop the alarm by being at the destination
    }
}

@Composable
private fun AlarmRingingScreen(
    context: Context,
    alarmName: String,
    targetLat: Double,
    targetLon: Double,
    radius: Int,
    onStop: () -> Unit
) {
    var distance by remember { mutableStateOf<Float?>(null) }
    var isWithinRadius by remember { mutableStateOf(false) }
    var isCheckingLocation by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Periodically check location
    LaunchedEffect(Unit) {
        while (true) {
            checkLocation(
                context = context,
                fusedLocationClient = fusedLocationClient,
                targetLat = targetLat,
                targetLon = targetLon,
                radius = radius,
                onResult = { dist, within ->
                    distance = dist
                    isWithinRadius = within
                    errorMessage = null
                },
                onError = { error ->
                    errorMessage = error
                }
            )
            delay(5000) // Check every 5 seconds
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = alarmName.ifBlank { "アラーム" },
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            } else if (distance != null) {
                if (isWithinRadius) {
                    Text(
                        text = "目的地に到着しました！",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "目的地まであと",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "${(distance!! - radius).toInt()} m",
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "位置情報を取得中...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Stop button - only enabled when within radius
            Button(
                onClick = {
                    if (isWithinRadius) {
                        onStop()
                    } else {
                        scope.launch {
                            isCheckingLocation = true
                            checkLocation(
                                context = context,
                                fusedLocationClient = fusedLocationClient,
                                targetLat = targetLat,
                                targetLon = targetLon,
                                radius = radius,
                                onResult = { dist, within ->
                                    distance = dist
                                    isWithinRadius = within
                                    isCheckingLocation = false
                                    if (within) {
                                        onStop()
                                    }
                                },
                                onError = { error ->
                                    errorMessage = error
                                    isCheckingLocation = false
                                }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isWithinRadius) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                ),
                enabled = !isCheckingLocation
            ) {
                if (isCheckingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isWithinRadius) "アラームを停止" else "位置を確認して停止",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (!isWithinRadius && distance != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "目的地の半径${radius}m以内に到着すると停止できます",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private suspend fun checkLocation(
    context: Context,
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    targetLat: Double,
    targetLon: Double,
    radius: Int,
    onResult: (Float, Boolean) -> Unit,
    onError: (String) -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onError("位置情報の権限がありません")
        return
    }

    try {
        val location = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).await()

        if (location != null) {
            val results = FloatArray(1)
            Location.distanceBetween(
                location.latitude,
                location.longitude,
                targetLat,
                targetLon,
                results
            )
            val distance = results[0]
            val isWithin = distance <= radius
            onResult(distance, isWithin)
        } else {
            onError("位置情報を取得できませんでした")
        }
    } catch (e: Exception) {
        onError("エラー: ${e.localizedMessage}")
    }
}
