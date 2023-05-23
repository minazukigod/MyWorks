package com.example.unspashaniskovtsev.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    val id: String,
    val likes: Int,
    val liked_by_user: Boolean,
    val user: User,
    val urls: Urls,
    val blur_hash: String?
)