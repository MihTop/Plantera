package com.example.plantera.data.pest

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface PestDao {
    @Insert
    suspend fun insert(pest: Pest)

    @Update
    suspend fun update(pest: Pest)

    @Delete
    suspend fun delete(pest: Pest)
}