package com.example.unspashaniskovtsev.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailPhoto(
    val exif: Exif?,
    val tags: List<Articles?>,
    val location: Location?,
    val likes: Int?,
    val downloads: Int?,
    val urls: Urls?,
    val liked_by_user: Boolean?,
    val blur_hash: String?,
    val user: User?

)
