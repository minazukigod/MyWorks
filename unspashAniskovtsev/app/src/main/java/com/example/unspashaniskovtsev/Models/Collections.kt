package com.example.unspashaniskovtsev.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Collections(
    val id: String,
    val total_photos: Int,
    val cover_photo: Photo,
    val title: String

)
