package com.example.plantera.data.repository

import com.example.plantera.data.bed.Bed
import com.example.plantera.data.bed.BedDao
import com.example.plantera.data.fertilizer.Fertilizer
import com.example.plantera.data.fertilizer.FertilizerDao
import com.example.plantera.data.pest.Pest
import com.example.plantera.data.pest.PestDao
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.plant.PlantDao
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class GardenRepository @Inject constructor(
    private val bedDao: BedDao,
    private val plantDao: PlantDao,
    private val pestDao: PestDao,
    private val fertilizerDao: FertilizerDao
) {
    // Методы грядок
    suspend fun insertBed(bed: Bed) = bedDao.insert(bed)
    suspend fun updateBed(bed: Bed) = bedDao.update(bed)
    suspend fun deleteBed(bed: Bed) = bedDao.delete(bed)

    // Методы растений
    suspend fun insertPlant(plant: Plant) = plantDao.insert(plant)
    suspend fun updatePlant(plant: Plant) = plantDao.update(plant)
    suspend fun deletePlant(plant: Plant) = plantDao.delete(plant)

    // Методы вредителей
    suspend fun insertPest(pest: Pest) = pestDao.insert(pest)
    suspend fun updatePest(pest: Pest) = pestDao.update(pest)
    suspend fun deletePest(pest: Pest) = pestDao.delete(pest)

    // Методы удобрений
    suspend fun insertFertilizer(fertilizer: Fertilizer) = fertilizerDao.insert(fertilizer)
    suspend fun updateFertilizer(fertilizer: Fertilizer) = fertilizerDao.update(fertilizer)
    suspend fun deleteFertilizer(fertilizer: Fertilizer) = fertilizerDao.delete(fertilizer)
}