package com.example.plantera.screen.pestBook

import androidx.lifecycle.ViewModel
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class PestBookScreenViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {

}