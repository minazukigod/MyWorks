package com.example.unspashaniskovtsev.data.Network

import retrofit2.Retrofit
import retrofit2.create

object Network {
    var token = ""
    lateinit var retrofit: Retrofit
    val unsplashApi: UnsplashApi
        get() = retrofit.create()
}