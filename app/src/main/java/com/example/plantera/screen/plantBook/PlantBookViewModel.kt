package com.example.plantera.screen.plantBook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.repository.GardenRepository
import com.example.plantera.data.repository.VegetationRepository
import com.example.plantera.data.response.VegetationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlantBookViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {
    val plants: Flow<List<Plant>> = repository.getAllPlants()

    private val vegRepository = VegetationRepository()
    val stationData = MutableStateFlow<VegetationResponse?>(null)

    fun populateBD(){
        viewModelScope.launch {
            repository.populateBD()
        }
    }

    fun loadStationByCode(code: String = "228310") {
        viewModelScope.launch {
            val data = vegRepository.getVegetationByStation(code)
            stationData.value = data
        }
    }
}