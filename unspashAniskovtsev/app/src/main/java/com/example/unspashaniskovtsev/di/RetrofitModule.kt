package com.example.unspashaniskovtsev.di

import android.content.Context
import com.example.unspashaniskovtsev.data.Network.Network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class RetrofitModule {
    @Provides
    fun providesClient(): OkHttpClient{
        return OkHttpClient.Builder()
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
    }
    @Provides
    fun providesRetrofit(): Retrofit{
        Network.retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(providesClient())
            .build()
        return Network.retrofit
    }
}