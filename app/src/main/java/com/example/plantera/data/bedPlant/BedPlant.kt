package com.example.plantera.data.bedPlant

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.plant.Plant

@Entity(
    tableName = "bed_plants",
    foreignKeys = [
        ForeignKey(
            entity = Bed::class,
            parentColumns = ["id"],
            childColumns = ["bedId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Plant::class,
            parentColumns = ["id"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BedPlant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bedId: Int,
    val plantId: Int,
    val plantingDate: String = "",
    val posX: Int? = null,
    val posY: Int? = null
)