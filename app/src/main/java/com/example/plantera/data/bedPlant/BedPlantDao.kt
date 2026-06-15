package com.example.plantera.data.bedPlant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.plantera.data.plant.Plant
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM bed_plants WHERE posX IS NULL AND bedId =:bedId")
    fun getUnplantedPlantWithId(bedId: Int): Flow<List<BedPlant>>

}