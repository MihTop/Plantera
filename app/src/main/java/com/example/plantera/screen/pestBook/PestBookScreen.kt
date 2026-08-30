package com.example.plantera.screen.pestBook

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.plantera.utils.EmojiUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PestBookScreen(
    navController: NavController,
    viewModel: PestBookViewModel = hiltViewModel()
){
    val pests by viewModel.pests.collectAsStateWithLifecycle(initialValue = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(pests, key = { it.id }) { pest ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFDE8E8)  // Светло-красный фон для вредителей
                ),
                onClick = { navController.navigate("pest_info_screen/${pest.id}") },
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
                        ) {
                            AsyncImage(
                                model = pest.image,
                                contentDescription = pest.name,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = pest.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                            Text(
                                text = pest.description,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = Color.Gray,
                                maxLines = 2
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text(
                            text = "Поражаемые растения:",
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                        ) {
                            val affectedPlants = pest.affectedPlants.split(" ").filter { it.isNotEmpty() }

                            affectedPlants.forEachIndexed { index, plantName ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxSize()
                                        .background(
                                            if (index % 2 == 0)
                                                Color(0xFFF44336)
                                            else
                                                Color(0xFFE53935)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = EmojiUtils.getPlantEmoji(plantName),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = plantName,
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}