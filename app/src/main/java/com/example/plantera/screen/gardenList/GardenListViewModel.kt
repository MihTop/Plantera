package com.example.plantera.screen.gardenList

import androidx.lifecycle.ViewModel
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class GardenListViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {
    val beds: Flow<List<Bed>> = repository.getAllBeds()
}