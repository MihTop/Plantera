package com.example.plantera.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.plantera.screen.fertilizer.FertilizerScreen
import com.example.plantera.screen.gardenInfo.GardenInfoScreen
import com.example.plantera.screen.gardenList.GardenListScreen
import com.example.plantera.screen.pestBook.PestBookScreen
import com.example.plantera.screen.pestInfo.PestInfoScreen
import com.example.plantera.screen.plantBook.PlantBookScreen
import com.example.plantera.screen.plantInfo.PlantInfoScreen
import com.example.plantera.screen.timetable.TimetableScreen

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
        composable(
            route = Screens.PlantInfo.route,
            arguments = listOf(navArgument("plantId") { type = NavType.IntType })
        ) {
            PlantInfoScreen(
                navController = navController
            )
        }
        composable(
            route = Screens.PestInfo.route,
            arguments = listOf(navArgument("pestId") { type = NavType.IntType })
        ) {
            PestInfoScreen(
                navController = navController
            )
        }
        composable(
            route = Screens.BedInfo.route,
            arguments = listOf(navArgument("bedId") { type = NavType.IntType })
        ) {
            GardenInfoScreen(
                navController = navController
            )
        }
        composable(route = Screens.Timetable.route) {
            TimetableScreen(navController)
        }
        composable(route = Screens.Fertilizer.route) {
            FertilizerScreen(navController)
        }
    }
}