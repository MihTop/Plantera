package com.example.plantera.data.plant

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var genus: String,
    var description: String,
    var tips: String,
    var soilTypeAndPh: String,
    val daysToMaturity: Int,
    val daysToSproutForTransplant: Int,
    var imageUrl: String? = null,
)
