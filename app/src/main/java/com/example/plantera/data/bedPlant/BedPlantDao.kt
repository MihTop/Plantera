package com.example.plantera.data.bedPlant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BedPlantDao {
    @Insert
    suspend fun insert(bedPlant: BedPlant): Long

    @Update
    suspend fun update(bedPlant: BedPlant)

    @Query("DELETE FROM bed_plants WHERE id = :bedPlantId")
    suspend fun deleteById(bedPlantId: Int)

    @Query("DELETE FROM bed_plants WHERE bedId = :bedId")
    suspend fun deleteAllBedPlantsByBed(bedId: Int)

}