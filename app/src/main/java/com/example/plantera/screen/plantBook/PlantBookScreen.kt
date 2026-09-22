package com.example.plantera.screen.plantBook

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.plantera.data.response.VegetationResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantBookScreen(
    navController: NavController,
    viewModel: PlantBookViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val plants by viewModel.plants.collectAsStateWithLifecycle(initialValue = emptyList())
    val stationData by viewModel.stationData.collectAsStateWithLifecycle(
        initialValue = VegetationResponse("0", 10, 360)
    )
    val station = stationData
    val firstWeight = station.avgStartDay.toFloat() / 365
    val lastWeight = (365 - station.avgEndDay).toFloat() / 365
    val vegWeight = 1f - firstWeight - lastWeight

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val stationNames by viewModel.stationNames.collectAsStateWithLifecycle()
    val weatherData by viewModel.weatherData.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    var showStationList by remember { mutableStateOf(false) }
    var showWeatherDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.loadWeatherByLocation()
            viewModel.loadNearestStation()
        }
    }

    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted) {
            viewModel.loadWeatherByLocation()
            viewModel.loadNearestStation()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (showWeatherDialog) {
        AlertDialog(
            onDismissRequest = { showWeatherDialog = false },
            title = { Text("Прогноз на 3 дня") },
            text = {
                Column {
                    weatherData?.forecast?.forecastday?.forEach { day ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatDate(day.date),
                                fontWeight = FontWeight.Medium
                            )
                            AsyncImage(
                                model = fixIconUrl(day.day.condition.icon),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(text = "${day.day.maxTempC.toInt()}° / ${day.day.minTempC.toInt()}°")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showWeatherDialog = false }) {
                    Text("Закрыть")
                }
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                showStationList = false
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Выбор станции",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (!showStationList) {
                    Button(
                        onClick = {
                            showBottomSheet = false
                            // Загружаем и станцию, и погоду — независимо
                            viewModel.loadNearestStation()
                            viewModel.loadWeatherByLocation()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text("По геолокации")
                    }

                    Button(
                        onClick = {
                            showStationList = true
                            if (stationNames.isEmpty()) {
                                viewModel.loadAllStationNames()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text("Из списка")
                    }
                } else {
                    if (isLoading && stationNames.isEmpty()) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(stationNames) { name ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                onClick = {
                                    viewModel.loadStationByName(name)
                                    showBottomSheet = false
                                    showStationList = false
                                }
                            ) {
                                Text(
                                    text = name,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = { showStationList = false },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Назад")
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        error?.let { err ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Button(onClick = {
                        viewModel.loadNearestStation()
                        viewModel.loadWeatherByLocation()
                    }) {
                        Text("Повторить")
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                weatherData?.current?.let { current ->
                    AsyncImage(
                        model = fixIconUrl(current.condition.icon),
                        contentDescription = "Погода",
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { showWeatherDialog = true }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.clickable { showWeatherDialog = true }) {
                        Text(
                            text = "${current.tempC.toInt()}°C",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = current.condition.text,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Станция",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = stationData.station,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Button(
            onClick = { viewModel.populateBD() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5E7A3C)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text("Заполнить БД", fontSize = 18.sp, color = Color.White)
        }

        Button(
            onClick = { showBottomSheet = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text("Выбрать станцию", fontSize = 18.sp, color = Color.White)
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(plants, key = { it.id }) { plant ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    onClick = { navController.navigate("plant_info_screen/${plant.id}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Card(
                                modifier = Modifier.size(80.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                AsyncImage(
                                    model = plant.imageUrl ?: "",
                                    contentDescription = plant.name,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = plant.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                                )
                                Text(
                                    text = plant.genus,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    color = Color.Gray
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                val months = listOf(
                                    "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
                                    "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
                                )
                                months.forEach { month ->
                                    Text(
                                        text = month,
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .padding(top = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(firstWeight)
                                        .fillMaxSize()
                                        .background(Color(0xFF2196F3))
                                        .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(vegWeight)
                                        .fillMaxSize()
                                        .background(Color.White)
                                        .border(1.dp, Color(0xFF2196F3))
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(lastWeight)
                                        .fillMaxSize()
                                        .background(Color(0xFF2196F3))
                                        .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun fixIconUrl(url: String): String =
    if (url.startsWith("//")) "https:$url" else url

private fun formatDate(isoDate: String): String {
    return try {
        val parts = isoDate.split("-")
        if (parts.size != 3) return isoDate
        val month = parts[1].toInt()
        val day = parts[2].toInt()
        val months = listOf(
            "янв", "фев", "мар", "апр", "май", "июн",
            "июл", "авг", "сен", "окт", "ноя", "дек"
        )
        "$day ${months[month - 1]}"
    } catch (e: Exception) {
        isoDate
    }
}