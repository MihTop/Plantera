package com.example.plantera.screen.gardenInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
fun GardenInfoScreen(
    navController: NavController,
    viewModel: GardenInfoViewModel = hiltViewModel()
){
    val bed by viewModel.bed.collectAsState()
    val unplantedPlants by viewModel.unplantedPlants.collectAsStateWithLifecycle(initialValue = emptyMap())
    val plantedPlants by viewModel.plantedPlants.collectAsStateWithLifecycle(initialValue = emptyList())
    var selectedUnPlant by remember { mutableIntStateOf(-1) }
    val selectedTiles = remember { mutableStateListOf<Pair<Int, Int>>() }
    var curUnPlantCount by remember { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Конкретная грядка",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Возврат"
                        )
                    }
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
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                val plantedMap = remember(plantedPlants) {
                    plantedPlants.associateBy { it.bedPlant.posX to it.bedPlant.posY }
                }

                Text(currentBed.name, modifier = Modifier.padding(8.dp))
                val horizontalScrollState = rememberScrollState()

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(horizontalScrollState)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(8.dp)
                    ) {
                        items(currentBed.tilesY) { y ->
                            Row {
                                repeat(currentBed.tilesX) { x ->
                                    val plant = plantedMap[x to y]
                                    val isSelected = selectedTiles.contains(Pair(x, y))

                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .padding(2.dp)
                                            .background(
                                                when {
                                                    plant != null -> Color(0xFFC8E6C9)
                                                    isSelected -> Color(0xFFFFF9C4)
                                                    else -> Color.LightGray
                                                }
                                            )
                                            .clickable {
                                                if (selectedUnPlant != -1 && curUnPlantCount > selectedTiles.size) {
                                                    if (plant == null && !isSelected) {
                                                        selectedTiles.add(Pair(x, y))
                                                    }
                                                }
                                            }
                                    ) {
                                        val textToShow = when {
                                            plant != null -> plant.plantName
                                            isSelected -> unplantedPlants[selectedUnPlant]?.firstOrNull()?.plantName
                                                ?: ""

                                            else -> ""
                                        }

                                        if (textToShow.isNotEmpty()) {
                                            Text(
                                                text = textToShow,
                                                modifier = Modifier.align(Alignment.Center),
                                                fontSize = 10.sp,
                                                lineHeight = 12.sp
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
        else{
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}