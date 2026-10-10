package com.example.plantera.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plantera.data.bed.Bed
import com.example.plantera.data.bed.BedDao
import com.example.plantera.data.bedPlant.BedPlant
import com.example.plantera.data.bedPlant.BedPlantDao
import com.example.plantera.data.fertilizer.Fertilizer
import com.example.plantera.data.fertilizer.FertilizerDao
import com.example.plantera.data.pest.Pest
import com.example.plantera.data.pest.PestDao
import com.example.plantera.data.plant.Plant
import com.example.plantera.data.plant.PlantDao
import com.example.plantera.data.timetable.Timetable
import com.example.plantera.data.timetable.TimetableDao

@Database(
    entities = [Bed::class, Plant::class, Pest::class, Fertilizer::class, BedPlant::class, Timetable::class],
    version = 10,
    exportSchema = false
)
abstract class GardenDatabase : RoomDatabase() {
    abstract fun bedDao(): BedDao
    abstract fun plantDao(): PlantDao
    abstract fun pestDao(): PestDao
    abstract fun fertilizerDao(): FertilizerDao
    abstract fun bedPlantDao(): BedPlantDao
    abstract fun timetableDao(): TimetableDao

    companion object {
        @Volatile
        private var INSTANCE: GardenDatabase? = null

        fun getDatabase(context: Context): GardenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GardenDatabase::class.java,
                    "garden.db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
