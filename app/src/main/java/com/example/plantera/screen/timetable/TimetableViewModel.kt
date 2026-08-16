package com.example.plantera.screen.timetable

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantera.data.repository.GardenRepository
import com.example.plantera.data.timetable.Timetable
import com.example.plantera.notification.NotificationReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.jvm.java

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: GardenRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val timetables: Flow<List<Timetable>> = repository.getAllTimetables()

    private val _permissionRequired = MutableStateFlow(false)
    val permissionRequired: StateFlow<Boolean> = _permissionRequired.asStateFlow()

    fun addTimetable(name: String, description: String, typeOfWork: String, date: String, time: String, equipment: String?) {
        viewModelScope.launch {
            repository.insertTimetable(Timetable(id = 0, name = name, description = description, typeOfWork = typeOfWork, date = date, time = time, equipment = equipment, isDone = false))
        }
    }

    fun updateDone(done: Boolean, id: Int) {
        viewModelScope.launch {
            repository.updateDone(done, id)
        }
    }

    fun scheduleNotification(
        title: String,
        message: String,
        day: Int,
        month: Int,
        year: Int,
        hour: Int,
        minute: Int
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        var triggerTime = calendar.timeInMillis

        if (triggerTime < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            triggerTime = calendar.timeInMillis
        }

        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
            putExtra("notificationId", notificationId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                _permissionRequired.value = true
                return
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }

    fun clearPermissionRequired() {
        _permissionRequired.value = false
    }
}