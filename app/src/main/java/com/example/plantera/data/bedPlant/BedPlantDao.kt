package com.example.plantera.data.bedPlant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    @Query("""
    SELECT bed_plants.*, plants.name as plantName, plants.imageUrl as plantImageUrl 
    FROM bed_plants 
    INNER JOIN plants ON bed_plants.plantId = plants.id 
    WHERE bed_plants.bedId = :bedId AND bed_plants.posX IS NULL""")
    fun getUnplantedPlantsWithId(bedId: Int): Flow<List<BedPlantWithInfo>>

    @Query("""
    SELECT bed_plants.*, plants.name as plantName, plants.imageUrl as plantImageUrl 
    FROM bed_plants 
    INNER JOIN plants ON bed_plants.plantId = plants.id 
    WHERE bed_plants.bedId = :bedId AND bed_plants.posX IS NOT NULL""")
    fun getPlantedPlantsWithId(bedId: Int): Flow<List<BedPlantWithInfo>>

    @Query("UPDATE bed_plants SET posX = :posX, posY = :posY WHERE id = :id")
    suspend fun updatePosition(id: Int, posX: Int, posY: Int)

    @Query("UPDATE bed_plants SET posX = null, posY = null WHERE id = :bedPlantId")
    suspend fun removePosition(bedPlantId: Int)

    @Query("UPDATE bed_plants SET posX = null, posY = null WHERE bedId = :bedId AND (posX >= :posX OR posY >= :posY)")
    suspend fun removeResizedPosition(bedId: Int, posX: Int, posY: Int)
}