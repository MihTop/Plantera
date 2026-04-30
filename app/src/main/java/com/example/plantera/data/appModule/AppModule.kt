package com.example.plantera.data.appModule

import android.content.Context
import com.example.plantera.data.bed.BedDao
import com.example.plantera.data.database.GardenDatabase
import com.example.plantera.data.fertilizer.FertilizerDao
import com.example.plantera.data.pest.PestDao
import com.example.plantera.data.plant.PlantDao
import com.example.plantera.data.repository.GardenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): GardenDatabase =
        GardenDatabase.getDatabase(app)

    @Provides
    @Singleton
    fun provideBedDao(db: GardenDatabase): BedDao = db.bedDao()

    @Provides
    @Singleton
    fun providePlantDao(db: GardenDatabase): PlantDao = db.plantDao()

    @Provides
    @Singleton
    fun providePestDao(db: GardenDatabase): PestDao = db.pestDao()

    @Provides
    @Singleton
    fun provideFertilizerDao(db: GardenDatabase): FertilizerDao = db.fertilizerDao()

    @Provides
    @Singleton
    fun provideGardenRepository(
        bedDao: BedDao,
        plantDao: PlantDao,
        pestDao: PestDao,
        fertilizerDao: FertilizerDao
    ): GardenRepository = GardenRepository(bedDao, plantDao, pestDao, fertilizerDao)
}