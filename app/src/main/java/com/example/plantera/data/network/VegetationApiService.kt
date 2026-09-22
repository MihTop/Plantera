package com.example.plantera.data.network

import com.example.plantera.data.response.VegetationResponse
import com.example.plantera.data.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VegetationApiService {

    @GET("api/vegetation/station/{name}")
    suspend fun getVegetationByStation(
        @Path("name") code: String
    ): VegetationResponse

    @GET("api/vegetation/region")
    suspend fun getVegetationByRegion(
        @Query("minLat") minLat: Double,
        @Query("maxLat") maxLat: Double,
        @Query("minLon") minLon: Double,
        @Query("maxLon") maxLon: Double
    ): List<VegetationResponse>

    @GET("api/vegetation/nearest")
    suspend fun getNearestStation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("maxDistanceKm") maxDistanceKm: Double = 100.0
    ): VegetationResponse

    @GET("api/vegetation/stations/names")
    suspend fun getStations(): List<String>
}