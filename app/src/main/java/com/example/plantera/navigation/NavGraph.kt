package com.example.plantera.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.plantera.screen.gardenList.GardenListScreen
import com.example.plantera.screen.pestBook.PestBookScreen
import com.example.plantera.screen.plantBook.PlantBookScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Screens.GardenList.route,
        modifier = modifier
    ) {
        composable(route = Screens.Pest.route) {
            PestBookScreen(navController)
        }
        composable(route = Screens.Plant.route) {
            PlantBookScreen(navController)
        }
        composable(route = Screens.GardenList.route) {
            GardenListScreen(navController)
        }
    }
}