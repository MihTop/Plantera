package com.example.plantera.data.network

import com.example.plantera.data.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 3,
        @Query("lang") lang: String = "ru"
    ): WeatherResponse
}