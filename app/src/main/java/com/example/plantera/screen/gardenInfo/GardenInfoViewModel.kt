package com.example.plantera.screen.gardenInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.bedPlant.BedPlantWithInfo
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.prefs.VegetationPrefs
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GardenInfoViewModel @Inject constructor(
    private val repository: GardenRepository,
    private val savedStateHandle: SavedStateHandle,
    private val vegetationPrefs: VegetationPrefs
) : ViewModel() {
    private val bedId: Int = checkNotNull(savedStateHandle["bedId"])

    private val _bed = MutableStateFlow<Bed?>(null)
    val bed: StateFlow<Bed?> = _bed.asStateFlow()

    val unplantedPlants: Flow<Map<Int, List<BedPlantWithInfo>>> = repository.getUnplantedPlantWithId(bedId).map { list ->
        list.groupBy { it.bedPlant.plantId }
    }

    val plantedPlants:  Flow<List<BedPlantWithInfo>> = repository.getPlantedPlantWithId(bedId)

    val vegetationStart: Int = vegetationPrefs.startDay
    val vegetationEnd: Int = vegetationPrefs.endDay

    init {
        loadBed()
    }

    private fun loadBed() {
        viewModelScope.launch {
            repository.getBedById(bedId).collect { bedFromDb ->
                _bed.value = bedFromDb
            }
        }
    }

    fun updatePlantPosition(bedPlantId: Int, posX: Int, posY: Int) {
        viewModelScope.launch {
            repository.updateBedPlantPosition(bedPlantId, posX, posY)
        }
    }

    fun updatePlantingDate(bedPlantId: Int, date: String) {
        viewModelScope.launch {
            repository.updateBedPlantPlantingDate(bedPlantId, date)
        }
    }

    fun removePlantPosition(bedPlantId: Int){
        viewModelScope.launch {
            repository.removeBedPlantPosition(bedPlantId)
        }
    }

    fun deletePlantPosition(bedPlantId: Int){
        viewModelScope.launch {
            repository.deleteBedPlant(bedPlantId)
        }
    }

    suspend fun getPlantById(plantId: Int): Plant? {
        return repository.getPlantById(plantId).first()
    }
}