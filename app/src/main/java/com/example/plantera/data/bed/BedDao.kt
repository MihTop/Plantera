package com.example.plantera.data.bed

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BedDao {
    @Insert
    suspend fun insert(bed: Bed)

    @Update
    suspend fun update(bed: Bed)

    @Delete
    suspend fun delete(bed: Bed)

    @Query("SELECT * FROM beds ORDER BY id DESC")
    fun getAllBeds(): Flow<List<Bed>>

    @Query("SELECT * FROM beds ORDER BY id DESC")
    suspend fun getAllBedsList(): List<Bed>

    @Query("SELECT * FROM beds WHERE id=:bedId")
    fun getBedById(bedId: Int): Flow<Bed?>
}