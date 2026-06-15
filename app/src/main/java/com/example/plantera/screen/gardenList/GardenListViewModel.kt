package com.example.plantera.screen.gardenList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class GardenListViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {
    val beds: Flow<List<Bed>> = repository.getAllBeds()

    fun addBed(name: String, xSize: Int, ySize: Int){
        viewModelScope.launch {
            repository.insertBed(Bed(id = 0, name = name, tilesX = xSize, tilesY = ySize))
        }
    }
}