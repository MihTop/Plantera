package com.example.plantera.data.timetable

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable")
data class Timetable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val typeOfWork: String,
    val date: String,
    val time: String,
    val equipment: String?,
    val isDone: Boolean
)
