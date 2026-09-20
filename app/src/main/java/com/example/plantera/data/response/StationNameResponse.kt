package com.example.plantera.data.response

import com.squareup.moshi.Json

data class StationNameResponse(
    @Json(name = "station")
    val station: String
)