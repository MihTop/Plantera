package com.example.plantera.data.repository

import com.example.plantera.data.network.VegetationApiService
import com.example.plantera.data.network.WeatherApiService
import com.example.plantera.data.response.WeatherResponse
import jakarta.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApiService: WeatherApiService
) {
    private val API_KEY = "3147be6ea630469b931111912262209"

    suspend fun getForecast(lat: Double, lon: Double): WeatherResponse {
        return weatherApiService.getForecast(
            apiKey = API_KEY,
            location = "$lat,$lon"
        )
    }
}