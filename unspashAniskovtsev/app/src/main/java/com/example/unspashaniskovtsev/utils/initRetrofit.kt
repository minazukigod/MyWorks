package com.example.unspashaniskovtsev.utils

import android.content.Context
import com.example.unspashaniskovtsev.data.Network.Network
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun initRetrofit(){

    val client: OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor {
            val original = it.request()
            val request = original.newBuilder()
                .addHeader("Authorization", " Bearer " + Network.token)
                .build()
            it.proceed(request)
        }
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    Network.retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
}