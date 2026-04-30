package com.example.plantera.data.bed

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BedDao {
    @Insert
    suspend fun insert(bed: Bed)

    @Update
    suspend fun update(bed: Bed)

    @Delete
    suspend fun delete(bed: Bed)
}