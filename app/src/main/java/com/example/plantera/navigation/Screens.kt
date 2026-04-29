package com.example.plantera.navigation

sealed class Screens(val route: String) {
    object GardenList: Screens("gardenList_screen")
    object Pest: Screens("pest_screen")
    object Plant: Screens("plant_screen")
}