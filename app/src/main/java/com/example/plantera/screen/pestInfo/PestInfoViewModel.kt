package com.example.plantera.screen.pestInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.pest.Pest
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.repository.GardenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PestInfoViewModel @Inject constructor(
    private val repository: GardenRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pestId: Int = checkNotNull(savedStateHandle["pestId"])

    private val _pest = MutableStateFlow<Pest?>(null)
    val pest: StateFlow<Pest?> = _pest.asStateFlow()

    init {
        loadPest()
    }

    private fun loadPest() {
        viewModelScope.launch {
            repository.getPestById(pestId).collect { pestFromDb ->
                _pest.value = pestFromDb
            }
        }
    }
}