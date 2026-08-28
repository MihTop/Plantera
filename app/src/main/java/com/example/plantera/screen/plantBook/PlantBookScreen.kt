package com.example.plantera.screen.plantBook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantBookScreen(
    navController: NavController,
    viewModel: PlantBookViewModel = hiltViewModel()
){
    val plants by viewModel.plants.collectAsStateWithLifecycle(initialValue = emptyList())

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
            items(plants, key = {it.id}){ plant ->
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
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = plant.name,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = plant.description)
                        Text(text = "Тута картинка: ${plant.description}")
                    }
                }
            }
        }
    }
}