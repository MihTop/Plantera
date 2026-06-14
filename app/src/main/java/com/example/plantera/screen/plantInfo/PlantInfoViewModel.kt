package com.example.plantera.screen.plantInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.bedPlant.BedPlant
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlantInfoViewModel @Inject constructor(
    private val repository: GardenRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val plantId: Int = checkNotNull(savedStateHandle["plantId"])

    private val _plant = MutableStateFlow<Plant?>(null)
    val plant: StateFlow<Plant?> = _plant.asStateFlow()

    init {
        loadPlant()
    }

    private fun loadPlant() {
        viewModelScope.launch {
            repository.getPlantById(plantId).collect { plantFromDb ->
                _plant.value = plantFromDb
            }
        }
    }

    suspend fun loadBeds(): List<Bed>{
        return repository.getAllBedsList()
    }

    fun insertBedPlant(bedPlant: BedPlant){
        viewModelScope.launch {
            repository.insertBedPlant(bedPlant)
        }
    }
}