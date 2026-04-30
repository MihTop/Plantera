package com.example.plantera.screen.plantBook

import androidx.lifecycle.ViewModel
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class PlantBookViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {

}