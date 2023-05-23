package com.example.unspashaniskovtsev.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val city: String?,
    val country: String?,
    val position: PositionLocation?
)

@JsonClass(generateAdapter = true)
data class PositionLocation (
    val latitude: Double?,
    val longitude: Double?
)
