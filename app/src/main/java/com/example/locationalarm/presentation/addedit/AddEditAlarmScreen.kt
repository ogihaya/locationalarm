package com.example.locationalarm.presentation.addedit

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAlarmScreen(
    alarmId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: AddEditAlarmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showTimePicker by remember { mutableStateOf(false) }

    // Default location (Tokyo)
    val defaultLocation = LatLng(35.6762, 139.6503)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            uiState.destination ?: defaultLocation,
            15f
        )
    }
    
    // Track if map is loaded to avoid CameraUpdateFactory being called before initialization
    var isMapLoaded by remember { mutableStateOf(false) }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            scope.launch {
                try {
                    val location = fusedLocationClient.lastLocation.await()
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        viewModel.updateDestination(latLng)
                        if (isMapLoaded) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(latLng, 17f)
                            )
                        }
                    }
                } catch (e: SecurityException) {
                    // Handle permission error
                }
            }
        }
    }

    // Navigate back when saved
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    // Update camera when destination changes (for edit mode)
    LaunchedEffect(uiState.destination, isMapLoaded) {
        if (isMapLoaded) {
            uiState.destination?.let { dest ->
                if (cameraPositionState.position.target != dest) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(dest, 16f)
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isEditMode) "アラームを編集" else "アラームを追加")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Alarm Name
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text("アラーム名（任意）") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        singleLine = true
                    )

                    // Time Picker
                    Card(
                        onClick = { showTimePicker = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    "時刻",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = String.format("%02d:%02d", uiState.timeHour, uiState.timeMinute),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Map Section
                    Text(
                        "目的地を選択",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        "マップをタップして目的地を設定してください",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp)
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            onMapClick = { latLng ->
                                viewModel.updateDestination(latLng)
                            },
                            onMapLoaded = {
                                isMapLoaded = true
                            },
                            properties = MapProperties(isMyLocationEnabled = false),
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = true,
                                myLocationButtonEnabled = false
                            )
                        ) {
                            uiState.destination?.let { dest ->
                                Marker(
                                    state = MarkerState(position = dest),
                                    title = "目的地"
                                )
                                Circle(
                                    center = dest,
                                    radius = uiState.radius.toDouble(),
                                    strokeColor = MaterialTheme.colorScheme.primary,
                                    fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    strokeWidth = 2f
                                )
                            }
                        }

                        // My Location Button
                        FloatingActionButton(
                            onClick = {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                                if (hasPermission) {
                                    scope.launch {
                                        try {
                                            val location = fusedLocationClient.lastLocation.await()
                                            location?.let {
                                                val latLng = LatLng(it.latitude, it.longitude)
                                                viewModel.updateDestination(latLng)
                                                cameraPositionState.animate(
                                                    CameraUpdateFactory.newLatLngZoom(latLng, 17f)
                                                )
                                            }
                                        } catch (e: SecurityException) {
                                            // Handle error
                                        }
                                    }
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp),
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            Icon(
                                Icons.Default.MyLocation,
                                contentDescription = "現在地",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Radius Slider
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "範囲: ${uiState.radius}m",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Slider(
                            value = uiState.radius.toFloat(),
                            onValueChange = { viewModel.updateRadius(it.toInt()) },
                            valueRange = 50f..500f,
                            steps = 8
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Save Button
                    Button(
                        onClick = { viewModel.saveAlarm() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        enabled = !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("保存")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = uiState.timeHour,
            initialMinute = uiState.timeMinute,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("時刻を選択") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateTime(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("キャンセル")
                }
            }
        )
    }

    // Error Dialog
    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("エラー") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }
}
