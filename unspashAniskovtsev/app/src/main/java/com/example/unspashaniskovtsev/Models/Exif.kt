package com.example.unspashaniskovtsev.Models

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Exif(
    val make: String?,
    val model: String?,
    val name: String?,
    val exposure_time: String?,
    val aperture: String?,
    val focal_length: String?,
    val iso: Int?
)


