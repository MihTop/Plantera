package com.example.plantera.screen.plantBook

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.repository.GardenRepository
import com.example.plantera.data.repository.VegetationRepository
import com.example.plantera.data.response.VegetationResponse
import com.example.plantera.utils.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlantBookViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: GardenRepository,
    private val vegRepository: VegetationRepository
) : ViewModel() {

    val plants: Flow<List<Plant>> = repository.getAllPlants()

    private val _stationData = MutableStateFlow(VegetationResponse("0", 10, 360))
    val stationData: StateFlow<VegetationResponse> = _stationData

    private val _stationNames = MutableStateFlow<List<String>>(emptyList())
    val stationNames: StateFlow<List<String>> = _stationNames

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun populateBD() {
        viewModelScope.launch {
            repository.populateBD()
        }
    }

    fun loadNearestStation(maxDistanceKm: Double = 100.0) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val location = LocationProvider.getCurrentLocation(context)
                if (location == null) {
                    _error.value = "Не удалось получить геолокацию"
                    return@launch
                }

                val (lat, lon) = location

                val data = vegRepository.getNearestStation(lat, lon, maxDistanceKm)
                _stationData.value = data

            } catch (e: retrofit2.HttpException) {
                if (e.code() == 404) {
                    _error.value = "Ближайшая станция дальше ${maxDistanceKm.toInt()} км"
                } else {
                    _error.value = "Ошибка сервера: ${e.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAllStationNames() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val names = vegRepository.getAllStationNames()
                _stationNames.value = names
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки станций: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadStationByName(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val data = vegRepository.getVegetationByStation(name)
                _stationData.value = data
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки станции: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}