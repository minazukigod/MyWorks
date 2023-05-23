package com.example.unspashaniskovtsev.data.Auth

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)
