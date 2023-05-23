package com.example.unspashaniskovtsev.data.Auth

import android.app.Application
import android.content.Context
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.utils.initRetrofit
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val context: Application
    )
{

    fun getAuthRequest(): AuthorizationRequest {
        return AppAuth.getAuthRequest()
    }

    fun logout(){
        TokenStorage.accesToken = null
        TokenStorage.idToken = null
        Network.token = ""
    }

     fun getEndSessionRequest(): EndSessionRequest{
         return AppAuth.getEndSessionRequest()
     }

    suspend fun perfromTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest
    ) {
        val tokens = AppAuth.performTokenRequestSuspend(authService, tokenRequest)
        Network.token = tokens.accessToken
        context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE).edit().putString("token", tokens.accessToken).commit()
        initRetrofit()
    }



}