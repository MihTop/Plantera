package com.example.plantera.screen.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorsScreen() {
    val context = LocalContext.current
    val sensorManager = remember(context) {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val lightSensor = remember(sensorManager) { sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) }
    val temperatureSensor = remember(sensorManager) {
        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    }
    val humiditySensor = remember(sensorManager) {
        sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    }
    var light by remember { mutableStateOf<Float?>(null) }
    var temperature by remember { mutableStateOf<Float?>(null) }
    var humidity by remember { mutableStateOf<Float?>(null) }

    DisposableEffect(sensorManager, lightSensor, temperatureSensor, humiditySensor) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_LIGHT -> light = event.values[0]
                    Sensor.TYPE_AMBIENT_TEMPERATURE -> temperature = event.values[0]
                    Sensor.TYPE_RELATIVE_HUMIDITY -> humidity = event.values[0]
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        listOfNotNull(lightSensor, temperatureSensor, humiditySensor).forEach { sensor ->
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        onDispose { sensorManager.unregisterListener(listener) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Датчики", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5E7A3C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF5F7F1)),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (lightSensor != null) {
                item {
                    SensorCard(
                        title = "Освещённость",
                        value = light?.let { formatValue(it) } ?: "—",
                        unit = "лк",
                        icon = Icons.Default.WbSunny,
                        iconColor = Color(0xFFF4AE18),
                        iconBackground = Color(0xFFFFF2CC)
                    ) {
                        if (light != null) {
                            val description = when {
                                light!! < 500f -> "Очень мало света"
                                light!! < 2500f -> "Подходит теневыносливым растениям"
                                light!! < 10000f -> "Умеренный свет для многих растений"
                                else -> "Яркий свет для светолюбивых растений"
                            }
                            Text(description, color = Color(0xFF465C35), fontWeight = FontWeight.Medium)
                        }
                        LightRanges()
                    }
                }
            }
            if (temperatureSensor != null) {
                item {
                    SensorCard(
                        title = "Температура воздуха",
                        value = temperature?.let { formatValue(it) } ?: "—",
                        unit = "°C",
                        icon = Icons.Default.Thermostat,
                        iconColor = Color(0xFFE56E45),
                        iconBackground = Color(0xFFFFE9DF)
                    )
                }
            }
            if (humiditySensor != null) {
                item {
                    SensorCard(
                        title = "Влажность воздуха",
                        value = humidity?.let { formatValue(it) } ?: "—",
                        unit = "%",
                        icon = Icons.Default.WaterDrop,
                        iconColor = Color(0xFF328DC7),
                        iconBackground = Color(0xFFDEF1FC)
                    )
                }
            }
        }
    }
}

private fun formatValue(value: Float): String = String.format(Locale.getDefault(), "%.1f", value)

@Composable
private fun SensorCard(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    iconColor: Color,
    iconBackground: Color,
    content: @Composable () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(iconBackground, CircleShape),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
                Column {
                    Text(title, color = Color(0xFF5D6857), style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "$value $unit",
                        color = Color(0xFF273726),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            content()
        }
    }
}

@Composable
private fun LightRanges() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF8E7), RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text("Ориентир по освещённости", fontWeight = FontWeight.SemiBold)
        Text("Менее 500 лк — слишком темно")
        Text("500–2 500 лк — теневыносливые растения")
        Text("2 500–10 000 лк — умеренный свет")
        Text("От 10 000 лк — светолюбивые растения")
        Text(
            "Потребность в свете зависит от растения и длительности освещения.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6D705D)
        )
    }
}
