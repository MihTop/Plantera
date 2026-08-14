package com.example.plantera.data.timetable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {
    @Insert
    suspend fun insert(timetable: Timetable)

    @Update
    suspend fun update(timetable: Timetable)

    @Delete
    suspend fun delete(timetable: Timetable)

    @Query("SELECT * FROM timetable ORDER BY id DESC")
    fun getAllTimetables(): Flow<List<Timetable>>
}