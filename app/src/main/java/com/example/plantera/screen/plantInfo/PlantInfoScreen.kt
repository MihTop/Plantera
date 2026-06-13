package com.example.plantera.screen.plantInfo

import android.R.attr.onClick
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.plantera.data.bed.Bed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantInfoScreen(
    navController: NavController,
    viewModel: PlantInfoViewModel = hiltViewModel()
){

    val plant by viewModel.plant.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var bedList by remember { mutableStateOf<List<Bed>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = { ExtendedFloatingActionButton(
            onClick = {
                showDialog = true
                scope.launch {
                    bedList = viewModel.loadBeds()
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
        if (showDialog == true){
            AddBedPlantDialog(
                onDismiss = {showDialog = false},
                bedList = bedList
            )
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(plant?.name.toString())
            Text(plant?.description.toString())
            Text(plant?.imageUrl.toString())
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBedPlantDialog(
    onDismiss: () -> Unit,
    bedList: List<Bed>
){
    Dialog(onDismissRequest = onDismiss) {
        LazyColumn() {
            items(bedList){ bed ->
                Box() {
                    Text(bed.name)
                }
            }
        }
    }
}