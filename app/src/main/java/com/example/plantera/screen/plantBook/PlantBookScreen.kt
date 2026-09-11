package com.example.plantera.screen.plantBook

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
){
    val plants by viewModel.plants.collectAsStateWithLifecycle(initialValue = emptyList())
    val stationData by viewModel.stationData.collectAsStateWithLifecycle(initialValue = VegetationResponse("0",10,360))
    val station = stationData ?: VegetationResponse("0", 10, 360)
    val firstWeight = station.avgStartDay.toFloat() / 365
    val lastWeight =  (365 - station.avgEndDay).toFloat() / 365
    val vegWeight =  1f - firstWeight - lastWeight

    LaunchedEffect(Unit) {
        viewModel.loadStationByCode("228310")
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
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
                                modifier = Modifier
                                    .size(80.dp),
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
                                val months = listOf("Янв", "Фев", "Мар", "Апр", "Май", "Июн",
                                    "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек")
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