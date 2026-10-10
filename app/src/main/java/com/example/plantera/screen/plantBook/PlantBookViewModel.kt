package com.example.plantera.screen.plantBook

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.prefs.VegetationPrefs
import com.example.plantera.data.repository.GardenRepository
import com.example.plantera.data.repository.VegetationRepository
import com.example.plantera.data.repository.WeatherRepository
import com.example.plantera.data.response.VegetationResponse
import com.example.plantera.data.response.WeatherResponse
import com.example.plantera.utils.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlantBookViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: GardenRepository,
    private val vegRepository: VegetationRepository,
    private val weatherRepository: WeatherRepository,
    private val vegetationPrefs: VegetationPrefs
) : ViewModel() {

    val plants: Flow<List<Plant>> = repository.getAllPlants()

    private val _stationNames = MutableStateFlow<List<String>>(emptyList())
    val stationNames: StateFlow<List<String>> = _stationNames

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    private val _vegetationStart = MutableStateFlow(vegetationPrefs.startDay)
    val vegetationStart: StateFlow<Int> = _vegetationStart.asStateFlow()

    private val _vegetationEnd = MutableStateFlow(vegetationPrefs.endDay)
    val vegetationEnd: StateFlow<Int> = _vegetationEnd.asStateFlow()

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
                _vegetationStart.value = data.avgStartDay
                _vegetationEnd.value = data.avgEndDay
                vegetationPrefs.save(
                    start = data.avgStartDay,
                    end = data.avgEndDay
                )
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
                _vegetationStart.value = data.avgStartDay
                _vegetationEnd.value = data.avgEndDay
                vegetationPrefs.save(
                    start = data.avgStartDay,
                    end = data.avgEndDay
                )
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки станции: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadWeatherByLocation() {
        viewModelScope.launch {
            try {
                val location = LocationProvider.getCurrentLocation(context)
                if (location == null) {
                    android.util.Log.e("Weather", "Не удалось получить геолокацию")
                    return@launch
                }
                val (lat, lon) = location
                android.util.Log.d("Weather", "Координаты: $lat, $lon")

                val data = weatherRepository.getForecast(lat, lon)
                _weatherData.value = data
                android.util.Log.d("Weather", "OK: ${data.current.tempC}°C, ${data.current.condition.text}")
            } catch (e: Exception) {
                android.util.Log.e("Weather", "Ошибка: ${e.javaClass.simpleName}: ${e.message}", e)
            }
        }
    }
}