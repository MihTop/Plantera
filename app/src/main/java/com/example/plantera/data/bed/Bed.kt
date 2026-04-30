package com.example.plantera.data.bed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beds")
data class Bed(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    val creationDate: String = "",
    val tilesX: Int,
    val tilesY: Int
)
