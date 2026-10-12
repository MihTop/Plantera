package com.example.plantera.data.repository

import com.example.plantera.data.bed.Bed
import com.example.plantera.data.bed.BedDao
import com.example.plantera.data.bedPlant.BedPlant
import com.example.plantera.data.bedPlant.BedPlantDao
import com.example.plantera.data.bedPlant.BedPlantWithInfo
import com.example.plantera.data.fertilizer.Fertilizer
import com.example.plantera.data.fertilizer.FertilizerDao
import com.example.plantera.data.pest.Pest
import com.example.plantera.data.pest.PestDao
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.plant.PlantDao
import com.example.plantera.data.timetable.Timetable
import com.example.plantera.data.timetable.TimetableDao
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlin.Int

@Singleton
class GardenRepository @Inject constructor(
    private val bedDao: BedDao,
    private val plantDao: PlantDao,
    private val pestDao: PestDao,
    private val fertilizerDao: FertilizerDao,
    private val bedPlantDao: BedPlantDao,
    private val timetableDao: TimetableDao
) {
    // Методы грядок
    suspend fun insertBed(bed: Bed) = bedDao.insert(bed)
    suspend fun updateBed(bed: Bed) = bedDao.update(bed)
    suspend fun deleteBed(bed: Bed) = bedDao.delete(bed)
    fun getAllBeds(): Flow<List<Bed>> = bedDao.getAllBeds()
    suspend fun getAllBedsList(): List<Bed> = bedDao.getAllBedsList()
    fun getBedById(bedId: Int): Flow<Bed?> = bedDao.getBedById(bedId)

    // Методы растений
    suspend fun insertPlant(plant: Plant) = plantDao.insert(plant)
    suspend fun updatePlant(plant: Plant) = plantDao.update(plant)
    suspend fun deletePlant(plant: Plant) = plantDao.delete(plant)
    fun getAllPlants(): Flow<List<Plant>> = plantDao.getAllPlants()
    fun getPlantById(plantId: Int): Flow<Plant?> = plantDao.getPlantById(plantId)

    // Методы вредителей
    suspend fun insertPest(pest: Pest) = pestDao.insert(pest)
    suspend fun updatePest(pest: Pest) = pestDao.update(pest)
    suspend fun deletePest(pest: Pest) = pestDao.delete(pest)
    fun getAllPests(): Flow<List<Pest>> = pestDao.getAllPests()
    fun getPestById(pestId: Int): Flow<Pest?> = pestDao.getPestById(pestId)

    // Методы удобрений
    suspend fun insertFertilizer(fertilizer: Fertilizer) = fertilizerDao.insert(fertilizer)
    suspend fun updateFertilizer(fertilizer: Fertilizer) = fertilizerDao.update(fertilizer)
    suspend fun deleteFertilizer(fertilizer: Fertilizer) = fertilizerDao.delete(fertilizer)
    fun getAllFertilizers(): Flow<List<Fertilizer>> = fertilizerDao.getAllFertilizers()

    // Методы растений на грядках
    suspend fun insertBedPlant(bedPlant: BedPlant) = bedPlantDao.insert(bedPlant)
    suspend fun updateBedPlantPosition(bedId: Int, posX: Int, posY: Int) = bedPlantDao.updatePosition(bedId,posX,posY)
    suspend fun updateBedPlantPlantingDate(bedId: Int, date: String) = bedPlantDao.updatePlantingDate(bedId,date)
    suspend fun removeBedPlantPosition(bedPlantId: Int) = bedPlantDao.removePosition(bedPlantId)
    suspend fun deleteBedPlant(bedPlantId: Int) = bedPlantDao.deleteById(bedPlantId)
    suspend fun removeResizedBedPlantPosition(bedId: Int, posX: Int, posY: Int) = bedPlantDao.removeResizedPosition(bedId,posX,posY)
    fun getUnplantedPlantWithId(bedId: Int): Flow<List<BedPlantWithInfo>> = bedPlantDao.getUnplantedPlantsWithId(bedId)
    fun getPlantedPlantWithId(bedId: Int): Flow<List<BedPlantWithInfo>> = bedPlantDao.getPlantedPlantsWithId(bedId)

    // Методы расписания на грядках
    suspend fun insertTimetable(timetable: Timetable) = timetableDao.insert(timetable)
    suspend fun updateDone(done: Boolean, id:Int) = timetableDao.updateDone(done,id)
    fun getAllTimetables(): Flow<List<Timetable>> = timetableDao.getAllTimetables()

    // Заполнение БД
    suspend fun populateBD(){
        insertBed(Bed(0,"Один",12,12))
        insertBed(Bed(0,"Два",3,2))
        insertPlant(Plant(
            name = "Капуста",
            genus = "Капустные (Brassicaceae)",
            description = "Овощная культура, богатая витаминами C и K, клетчаткой. Используется в свежем виде, для квашения и термической обработки.",
            tips = "Поливать 2-3 раза в неделю. Предпочитает солнечные места. Оптимальная температура 15-20°C. Собирать урожай при плотных кочанах.",
            soilTypeAndPh = "Суглинистая почва, pH 6.0-7.0. Требует обильного полива в период формирования кочана.",
            daysToMaturity = 120,
            daysToSproutForTransplant = 45,
            imageUrl = "https://ss"
        ))
        insertPlant(Plant(
            name = "Томат",
            genus = "Паслёновые (Solanaceae)",
            description = "Теплолюбивая овощная культура с сочными плодами. Выращивается через рассаду для получения урожая в открытом грунте.",
            tips = "Высаживать после окончания заморозков. Поливать под корень, регулярно пасынковать высокорослые сорта и подвязывать стебли.",
            soilTypeAndPh = "Лёгкая плодородная почва, pH 6.0-6.8. Нужны солнечное место и защита от холодного ветра.",
            daysToMaturity = 110,
            daysToSproutForTransplant = 55,
            imageUrl = "https://ss"
        ))
        insertPest(Pest(0,"Картофелеед","Кушает картошку","Картофель Перец","Купить мышь", "https:yss"))
        insertPest(Pest(0,"Гниение","Убивает растение","Картофель Перец Огурец Клубника","Меньше поливать", "https:yss"))
        insertFertilizer(Fertilizer(0,"Сода"))
        insertFertilizer(Fertilizer(0,"Сера"))
    }

}
