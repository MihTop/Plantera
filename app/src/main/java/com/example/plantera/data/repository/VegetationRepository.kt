package com.example.plantera.data.repository

import com.example.plantera.data.network.VegetationApiService
import com.example.plantera.data.response.VegetationResponse
import jakarta.inject.Inject

class VegetationRepository @Inject constructor(
    private val apiService: VegetationApiService
) {

    suspend fun getVegetationByStation(code: String): VegetationResponse {
        return apiService.getVegetationByStation(code)
    }

    suspend fun getVegetationByRegion(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): List<VegetationResponse> {
        return apiService.getVegetationByRegion(minLat, maxLat, minLon, maxLon)
    }

    suspend fun getNearestStation(
        lat: Double,
        lon: Double,
        maxDistanceKm: Double = 100.0
    ): VegetationResponse =
        apiService.getNearestStation(lat, lon, maxDistanceKm)

    suspend fun getAllStationNames(): List<String>{
        return apiService.getStations()
    }
}