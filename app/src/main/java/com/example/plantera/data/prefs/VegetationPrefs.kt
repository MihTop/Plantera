package com.example.plantera.data.prefs

import android.content.Context
import androidx.core.content.edit

class VegetationPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("vegetation_prefs", Context.MODE_PRIVATE)

    var startDay: Int
        get() = prefs.getInt(KEY_START, 10)
        set(value) = prefs.edit { putInt(KEY_START, value) }

    var endDay: Int
        get() = prefs.getInt(KEY_END, 360)
        set(value) = prefs.edit { putInt(KEY_END, value) }

    fun save(start: Int, end: Int) {
        prefs.edit {
            putInt(KEY_START, start)
            putInt(KEY_END, end)
        }
    }

    companion object {
        private const val KEY_START = "vegetation_start_day"
        private const val KEY_END = "vegetation_end_day"
    }
}