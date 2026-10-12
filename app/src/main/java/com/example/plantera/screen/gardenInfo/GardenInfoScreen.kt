package com.example.plantera.screen.gardenInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.plantera.data.plant.Plant
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

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
    val scope = rememberCoroutineScope()
    var selectedFullPlant by remember { mutableStateOf<Plant?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var editingPlantId by remember { mutableIntStateOf(-1) }

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
                                    var expanded by remember { mutableStateOf(false) }
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
                                                } else if (selectedUnPlant == -1){
                                                    if (plant != null) {
                                                        scope.launch {
                                                            selectedFullPlant = viewModel.getPlantById(plant.bedPlant.plantId)
                                                        }
                                                    }
                                                    expanded = true
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
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = {
                                            expanded = false
                                            selectedFullPlant = null
                                        }
                                    ) {
                                        if (plant != null) {
                                            selectedFullPlant?.let { fullPlant ->
                                                DropdownMenuItem(
                                                    text = {
                                                        PlantSchedule(
                                                            plant = fullPlant,
                                                            plantingDate = plant.bedPlant.plantingDate,
                                                            vegetationStartDay = viewModel.vegetationStart,
                                                            vegetationEndDay = viewModel.vegetationEnd
                                                        )
                                                    },
                                                    onClick = {
                                                        expanded = false
                                                    }
                                                )
                                            }
                                            DropdownMenuItem(
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.Default.CalendarMonth,
                                                        contentDescription = "Смена даты"
                                                    )
                                                },
                                                onClick = {
                                                    editingPlantId = plant.bedPlant.id
                                                    showDialog = true
                                                    expanded = false
                                                },
                                                text = { Text(text = "Посажено: ${plant.bedPlant.plantingDate}")}
                                            )
                                            DropdownMenuItem(
                                                    leadingIcon = {
                                                        Icon(
                                                            Icons.Default.Delete,
                                                            contentDescription = "Удалить"
                                                        )
                                                    },
                                            onClick = {
                                                viewModel.deletePlantPosition(plant.bedPlant.id)
                                                expanded = false
                                            },
                                            text = { Text(text = "Удалить", color = Color.Red) }
                                            )
                                            DropdownMenuItem(
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.AutoMirrored.Filled.Redo,
                                                        contentDescription = "В непосаженные"
                                                    )
                                                },
                                                onClick = {
                                                    viewModel.removePlantPosition(plant.bedPlant.id)
                                                    expanded = false
                                                },
                                                text = { Text(text = "В непосаженные", color = Color.Red) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                LazyRow() {
                    items(unplantedPlants.toList()) { (unPlantId, unPlant) ->
                        Box(
                            Modifier
                                .padding(2.dp)
                                .border(2.dp, Color.Black)
                                .clickable {
                                    selectedUnPlant = unPlantId
                                    curUnPlantCount = unPlant.size
                                }
                        ) {
                            Text(unPlant[0].plantName + " Ост: " + unPlant.size)
                        }
                    }
                }
                if (selectedUnPlant != -1) {
                    Button(onClick = {
                        val plantsToPlant = unplantedPlants[selectedUnPlant] ?: emptyList()

                        selectedTiles.forEachIndexed { index, coords ->
                            if (index < plantsToPlant.size) {
                                val plantRecord = plantsToPlant[index]
                                viewModel.updatePlantPosition(
                                    plantRecord.bedPlant.id,
                                    coords.first,
                                    coords.second
                                )
                            }
                        }
                        selectedTiles.clear()
                        selectedUnPlant = -1
                    }) {
                        Text("Посадить растения")
                    }
                }
            }
            if (showDialog) {
                DatePickerModal(
                    onDismiss = { showDialog = false },
                    viewModel = viewModel,
                    id = editingPlantId
                )
            }
        }
        else{
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
@Composable
private fun PlantSchedule(
    plant: Plant,
    plantingDate: String,
    vegetationStartDay: Int,
    vegetationEndDay: Int
) {
    val plantingDay = remember(plantingDate) {
        runCatching { LocalDate.parse(plantingDate).dayOfYear }.getOrNull()
    } ?: vegetationStartDay

    val firstHarvestDay = plantingDay + plant.daysToMaturity
    val latestPlantingDay = vegetationEndDay - plant.daysToMaturity

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            NegativeTemperaturePeriod(vegetationStartDay, vegetationEndDay)
            YearRange(
                startDay = plantingDay,
                endDay = firstHarvestDay,
                color = Color(0xFF66BB6A)
            )
            YearRange(
                startDay = firstHarvestDay,
                endDay = vegetationEndDay,
                color = Color(0xFFAB47BC)
            )
        }
        TodayMarker()
    }
}
@Composable
private fun NegativeTemperaturePeriod(
    vegetationStartDay: Int,
    vegetationEndDay: Int
) {
    val firstWeight = vegetationStartDay.coerceIn(0, 365).toFloat() / 365f
    val lastWeight = (365 - vegetationEndDay.coerceIn(0, 365)).toFloat() / 365f
    val vegetationWeight = 1f - firstWeight - lastWeight

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
    ) {
        if (firstWeight > 0f) {
            Box(
                modifier = Modifier
                    .weight(firstWeight)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .background(Color(0xFF90CAF9))
            )
        }
        if (vegetationWeight > 0f) Spacer(modifier = Modifier.weight(vegetationWeight))
        if (lastWeight > 0f) {
            Box(
                modifier = Modifier
                    .weight(lastWeight)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(Color(0xFF90CAF9))
            )
        }
    }
}

@Composable
private fun YearRange(
    startDay: Int,
    endDay: Int,
    color: Color
) {
    val safeStartDay = startDay.coerceIn(0, 365)
    val safeEndDay = endDay.coerceIn(safeStartDay, 365)
    val beforeWeight = safeStartDay.toFloat() / 365f
    val rangeWeight = (safeEndDay - safeStartDay).toFloat() / 365f
    val afterWeight = 1f - beforeWeight - rangeWeight

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
    ) {
        if (beforeWeight > 0f) Spacer(modifier = Modifier.weight(beforeWeight))
        if (rangeWeight > 0f) {
            Box(
                modifier = Modifier
                    .weight(rangeWeight)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
        if (afterWeight > 0f) Spacer(modifier = Modifier.weight(afterWeight))
    }
}

@Composable
private fun BoxScope.TodayMarker() {
    val dayOfYear = LocalDate.now().dayOfYear
    val weight = dayOfYear / 365f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .matchParentSize()
    ) {
        if (weight > 0f) Spacer(modifier = Modifier.weight(weight))
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.Black)
        )
        val afterWeight = 1f - weight
        if (afterWeight > 0f) Spacer(modifier = Modifier.weight(afterWeight))
    }
}
@Composable
fun DatePickerModal(
    onDismiss: () -> Unit,
    viewModel: GardenInfoViewModel,
    id: Int
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val millis = datePickerState.selectedDateMillis
                if (millis != null) {
                    val dateString = Instant.ofEpochMilli(millis)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()
                        .toString()
                    viewModel.updatePlantingDate(id, dateString)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}