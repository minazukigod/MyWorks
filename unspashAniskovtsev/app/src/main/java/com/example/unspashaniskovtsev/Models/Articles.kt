package com.example.unspashaniskovtsev.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Articles(
    val title: String
)
