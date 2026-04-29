package com.example.plantera.screen.plantBook

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun PlantBookScreen(
    navController: NavController,
    viewModel: PlantBookViewModel = hiltViewModel()
){
    Text(
        text = "PlantBookScreen"
    )
}