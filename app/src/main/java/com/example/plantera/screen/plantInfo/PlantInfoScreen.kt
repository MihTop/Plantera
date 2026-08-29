package com.example.plantera.screen.plantInfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.plant.Plant
import com.example.plantera.utils.EmojiUtils
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
        onInsertBedPlant = { viewModel.insertBedPlant(it) },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantInfoScreenContent(
    plant: Plant?,
    onLoadBeds: suspend () -> List<Bed>,
    onInsertBedPlant: (Int) -> Unit,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }
    var bedList by remember { mutableStateOf<List<Bed>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showDialog = true
                    scope.launch {
                        bedList = onLoadBeds()
                    }
                },
                icon = { Icon(Icons.Default.Add, "Добавление растения") },
                text = { Text(text = "Добавить на грядку") },
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        plant?.name ?: "Информация о растении",
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
        if (showDialog){
            AddBedPlantDialog(
                onDismiss = { showDialog = false },
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .size(120.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    AsyncImage(
                        model = plant?.imageUrl ?: "",
                        contentDescription = plant?.name ?: "Изображение растения",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = plant?.name ?: "Название",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                    Text(
                        text = "Род: ${plant?.genus ?: "Не указан"}",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "Описание:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = plant?.description ?: "Описание отсутствует",
                    modifier = Modifier.padding(16.dp),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }

            Text(
                text = "Советы по уходу:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = plant?.tips ?: "Советы отсутствуют",
                    modifier = Modifier.padding(16.dp),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }

            Text(
                text = "Тип почвы и pH:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = plant?.soilTypeAndPh ?: "Информация отсутствует",
                    modifier = Modifier.padding(16.dp),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
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
        Surface(
            modifier = Modifier
                .shadow(2.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Выберите грядку для посадки растения",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    items(bedList) { bed ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onBedClick(bed.id)
                                },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = EmojiUtils.getPlantEmoji(bed.name),
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                )
                                Text(
                                    text = bed.name,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}