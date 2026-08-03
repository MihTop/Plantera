package com.example.plantera.screen.gardenInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.bedPlant.BedPlant
import com.example.plantera.data.bedPlant.BedPlantWithInfo
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class GardenInfoViewModel @Inject constructor(
    private val repository: GardenRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bedId: Int = checkNotNull(savedStateHandle["bedId"])

    private val _bed = MutableStateFlow<Bed?>(null)
    val bed: StateFlow<Bed?> = _bed.asStateFlow()

    val unplantedPlants: Flow<Map<Int, List<BedPlantWithInfo>>> = repository.getUnplantedPlantWithId(bedId).map { list ->
        list.groupBy { it.bedPlant.plantId }
    }

    val plantedPlants:  Flow<List<BedPlantWithInfo>> = repository.getPlantedPlantWithId(bedId)

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

    // В GardenInfoViewModel.kt
    fun updatePlantPosition(bedPlantId: Int, posX: Int, posY: Int) {
        viewModelScope.launch {
            repository.updateBedPlantPosition(bedPlantId, posX, posY)
        }
    }

}