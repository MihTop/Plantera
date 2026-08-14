package com.example.plantera.navigation

sealed class Screens(val route: String) {
    object GardenList: Screens("gardenList_screen")
    object Pest: Screens("pest_screen")
    object Plant: Screens("plant_screen")
    object PlantInfo: Screens("plant_info_screen/{plantId}")
    object PestInfo: Screens("pest_info_screen/{pestId}")
    object BedInfo: Screens("bed_info_screen/{bedId}")
    object Timetable: Screens("timetable_screen")
    object Fertilizer: Screens("fertilizer_screen")
}