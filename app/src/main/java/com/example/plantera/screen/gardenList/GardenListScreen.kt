package com.example.plantera.screen.gardenList

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun GardenListScreen(
    navController: NavController,
    viewModel: GardenListViewModel = hiltViewModel()
){
    Text(
        text = "GardenListScreen"
    )
}