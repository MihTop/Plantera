package com.example.plantera.screen.gardenInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.plantera.screen.pestInfo.PestInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenInfoScreen(
    navController: NavController,
    viewModel: GardenInfoViewModel = hiltViewModel()
){
    val bed by viewModel.bed.collectAsState()
    val unplantedPlants by viewModel.unplantedPlants.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Конкретная грядка",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5E7A3C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        val currentBed = bed
        if (currentBed != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Text(currentBed.name)
                Text(currentBed.creationDate)

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(currentBed.tilesY) { y ->
                        LazyRow {
                            items(currentBed.tilesX) { x ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(2.dp)
                                        .background(Color.LightGray)
                                ) {
                                    Text(
                                        text = "v",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
                LazyRow(Modifier.fillMaxWidth()) {
                    items(unplantedPlants){ unPlant ->
                        Box() {
                            Text(unPlant.plantId.toString())
                        }
                    }
                }
                Text("Количество плиток по Y: ${currentBed.tilesY}")
            }
        }
        else{
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}