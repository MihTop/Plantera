package com.example.plantera.screen.pestBook

import androidx.lifecycle.ViewModel
import com.example.plantera.data.pest.Pest
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class PestBookViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {
    val pests: Flow<List<Pest>> = repository.getAllPests()
}