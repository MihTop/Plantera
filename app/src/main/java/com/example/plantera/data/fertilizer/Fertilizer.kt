package com.example.plantera.data.fertilizer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fertilizers")
data class Fertilizer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String
)
