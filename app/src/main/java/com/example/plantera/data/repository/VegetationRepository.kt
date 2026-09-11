package com.example.plantera.data.repository

import com.example.plantera.data.network.RetrofitClient
import com.example.plantera.data.response.VegetationResponse

class VegetationRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getAllVegetation(): List<VegetationResponse> {
        return apiService.getAllVegetation()
    }

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
}