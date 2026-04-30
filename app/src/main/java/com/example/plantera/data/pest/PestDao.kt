package com.example.plantera.data.pest

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PestDao {
    @Insert
    suspend fun insert(pest: Pest)

    @Update
    suspend fun update(pest: Pest)

    @Delete
    suspend fun delete(pest: Pest)

    @Query("SELECT * FROM pests ORDER BY id DESC")
    fun getAllPests(): Flow<List<Pest>>
}