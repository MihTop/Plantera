package com.example.plantera.screen.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.repository.GardenRepository
import com.example.plantera.data.timetable.Timetable
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: GardenRepository
) : ViewModel() {
    val timetables: Flow<List<Timetable>> = repository.getAllTimetables()

    fun addTimetable(name: String, description: String, typeOfWork: String, date: String, time: String, equipment: String?){
        viewModelScope.launch {
            repository.insertTimetable(Timetable(id = 0, name = name, description = description, typeOfWork = typeOfWork, date = date, time = time,equipment = equipment))
        }
    }
}