package com.example.plantera.data.fertilizer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface FertilizerDao {
    @Insert
    suspend fun insert(fertilizer: Fertilizer)

    @Update
    suspend fun update(fertilizer: Fertilizer)

    @Delete
    suspend fun delete(fertilizer: Fertilizer)
}