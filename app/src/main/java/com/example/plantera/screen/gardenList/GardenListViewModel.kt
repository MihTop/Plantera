package com.example.plantera.screen.gardenList

import androidx.lifecycle.ViewModel
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class GardenListViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {

}