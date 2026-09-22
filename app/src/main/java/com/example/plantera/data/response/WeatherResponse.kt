package com.example.plantera.data.response

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "current") val current: CurrentWeather,
    @Json(name = "forecast") val forecast: Forecast
)

data class CurrentWeather(
    @Json(name = "temp_c") val tempC: Double,
    @Json(name = "condition") val condition: WeatherCondition
)

data class Forecast(
    @Json(name = "forecastday") val forecastday: List<ForecastDay>
)

data class ForecastDay(
    @Json(name = "date") val date: String,
    @Json(name = "day") val day: DayForecast
)

data class DayForecast(
    @Json(name = "maxtemp_c") val maxTempC: Double,
    @Json(name = "mintemp_c") val minTempC: Double,
    @Json(name = "condition") val condition: WeatherCondition
)

data class WeatherCondition(
    @Json(name = "text") val text: String,
    @Json(name = "icon") val icon: String // URL иконки
)