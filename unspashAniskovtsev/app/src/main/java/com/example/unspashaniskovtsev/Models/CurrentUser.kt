package com.example.unspashaniskovtsev.Models

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CurrentUser(
    val id: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val downloads: String,
    val email: String,
    val location: String?
    )
