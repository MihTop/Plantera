package com.example.plantera.navigation

sealed class Screens(val route: String) {
    object GardenList: Screens("gardenList_screen")
    object PlantInfo: Screens("plant_info_screen/{plantId}")
    object PestInfo: Screens("pest_info_screen/{pestId}")
    object BedInfo: Screens("bed_info_screen/{bedId}")
    object Timetable: Screens("timetable_screen")
    object BookPicker: Screens("book_picker_screen")
    object Sensors: Screens("sensors_screen")
}
