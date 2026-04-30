package com.example.plantera.screen.plantBook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class PlantBookViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {
    val plants: Flow<List<Plant>> = repository.getAllPlants()

    //Тестовая функция
    fun populateBD(){
        viewModelScope.launch {
            repository.populateBD()
        }
    }

}