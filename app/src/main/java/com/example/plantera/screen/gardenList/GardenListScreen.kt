package com.example.plantera.screen.gardenList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenListScreen(
    navController: NavController,
    viewModel: GardenListViewModel = hiltViewModel()
){
    val beds by viewModel.beds.collectAsStateWithLifecycle(initialValue = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = { ExtendedFloatingActionButton(
            onClick = {
                showAddDialog = true
            },
            icon = { Icon(Icons.Default.Add,"Добавление грядки") },
            text = { Text(text = "Добавить грядку") },
        ) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Грядки",
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
        if (showAddDialog){
            AddBedDialog(onDismiss = {showAddDialog = false},
                onAdd = { bedName, bedX, bedY ->
                    viewModel.addBed(bedName,bedX,bedY)
                })
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(beds, key = {it.id}){ bed ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF98FA9F)
                        ),
                        onClick = { navController.navigate("bed_info_screen/${bed.id}") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Text(bed.name)
                        Row {
                            Text("Размер по X:${bed.tilesX}")
                            Text("Размер по Y:${bed.tilesY}")
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBedDialog(
    onDismiss: () -> Unit,
    onAdd: (String,Int,Int) -> Unit,
){
    Dialog(onDismissRequest = onDismiss) {
        val bedName = remember{mutableStateOf("")}
        val bedXSize = remember{mutableStateOf("")}
        val bedYSize = remember{mutableStateOf("")}
        Surface(modifier = Modifier.shadow(2.dp)) {
            Column() {
                Text("Введите параметры грядки")
                OutlinedTextField(value = bedName.value,
                    onValueChange = {bedName.value = it},
                )
                OutlinedTextField(value = bedXSize.value,
                    onValueChange = {bedXSize.value = it},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                OutlinedTextField(value = bedYSize.value,
                    onValueChange = {bedYSize.value = it},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Button(onClick = { onAdd(bedName.value,bedXSize.value.toInt(),bedYSize.value.toInt())
                onDismiss()}) {
                    Text("Добавить")
                }
            }
        }
    }
}