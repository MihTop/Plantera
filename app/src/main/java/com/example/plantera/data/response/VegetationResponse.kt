package com.example.plantera.data.response

import com.squareup.moshi.Json

data class VegetationResponse(
    @Json(name = "station")
    val station: String,

    @Json(name = "avgStartDay")
    val avgStartDay: Int,

    @Json(name = "avgEndDay")
    val avgEndDay: Int
)