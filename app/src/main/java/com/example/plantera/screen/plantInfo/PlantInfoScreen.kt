package com.example.plantera.screen.plantInfo

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.plant.Plant
import kotlinx.coroutines.launch

@Composable
fun PlantInfoScreen(
    navController: NavController,
    viewModel: PlantInfoViewModel = hiltViewModel()
){
    val plant by viewModel.plant.collectAsState()
    PlantInfoScreenContent(
        plant = plant,
        onLoadBeds = { viewModel.loadBeds() },
        onInsertBedPlant = { viewModel.insertBedPlant(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantInfoScreenContent(
    plant: Plant?,
    onLoadBeds: suspend () -> List<Bed>,
    onInsertBedPlant: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var bedList by remember { mutableStateOf<List<Bed>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = { ExtendedFloatingActionButton(
            onClick = {
                showDialog = true
                scope.launch {
                    bedList = onLoadBeds()
                }
            },
            icon = { Icon(Icons.Default.Add,"Добавление растения") },
            text = { Text(text = "Добавить на грядку") },
        ) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Конкретное растение",
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
        if (showDialog){
            AddBedPlantDialog(
                onDismiss = {showDialog = false},
                onBedClick = { bedId ->
                    onInsertBedPlant(bedId)
                    showDialog = false
                },
                bedList = bedList,
            )
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(plant?.name ?: "")
            Text(plant?.description ?: "")
            Text(plant?.imageUrl ?: "")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBedPlantDialog(
    onDismiss: () -> Unit,
    onBedClick: (Int) -> Unit,
    bedList: List<Bed>
){
    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = Modifier.shadow(2.dp)) {
            Column() {
                Text("Выберите грядку для посадки растения")
                LazyColumn() {
                    items(bedList){ bed ->
                        Row(modifier = Modifier.border(1.dp, Color.Black)
                            .clickable{
                                onBedClick(bed.id)
                            }
                        ) {
                            Text(bed.name)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlantInfoScreenPreview() {
    PlantInfoScreenContent(
        plant = Plant(1, "Томат", "Вкусный и сочный овощ для вашей грядки", null),
        onLoadBeds = { emptyList() },
        onInsertBedPlant = {}
    )
}