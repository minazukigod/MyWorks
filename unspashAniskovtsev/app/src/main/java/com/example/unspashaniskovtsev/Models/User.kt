package com.example.unspashaniskovtsev.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val username: String,
    val profile_image: ProfileImage,
    val bio: String?

)

