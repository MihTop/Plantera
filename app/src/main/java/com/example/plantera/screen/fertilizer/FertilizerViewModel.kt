package com.example.plantera.screen.fertilizer

import androidx.lifecycle.ViewModel
import com.example.plantera.data.fertilizer.Fertilizer
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class FertilizerViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {
    val fertilizer: Flow<List<Fertilizer>> = repository.getAllFertilizers()
}