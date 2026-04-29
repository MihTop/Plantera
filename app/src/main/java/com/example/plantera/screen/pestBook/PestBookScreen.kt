package com.example.plantera.screen.pestBook

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.plantera.screen.gardenList.GardenListViewModel

@Composable
fun PestBookScreen(
    navController: NavController,
    viewModel: PestBookScreenViewModel = hiltViewModel()
){
    Text(
        text = "PestBookScreen"
    )
}