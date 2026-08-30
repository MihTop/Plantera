package com.example.plantera.data.pest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pests")
data class Pest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val affectedPlants: String,
    val prevention: String,
    val image: String
)