package com.example.unspashaniskovtsev.data.Auth

import android.net.Uri
import androidx.core.net.toUri
import com.example.unspashaniskovtsev.data.Network.Network
import net.openid.appauth.*
import kotlin.coroutines.suspendCoroutine

object AppAuth {

    private val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI),
        null,
        Uri.parse(AuthConfig.END_SESSION_URI)
    )
    fun getAuthRequest(): AuthorizationRequest {
        val redirectUri = AuthConfig.CALLBACK_URL.toUri()

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(tokenRequest, getClientAuthentication()) { response, ex ->
                when {
                    response != null -> {
                        //получение токена произошло успешно
                        val tokens = TokensModel(
                            accessToken = response.accessToken.orEmpty(),
                            refreshToken = response.refreshToken.orEmpty(),
                            idToken = response.idToken.orEmpty()
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }
                    //получение токенов произошло неуспешно, показываем ошибку
                    ex != null -> { continuation.resumeWith(Result.failure(ex)) }
                    else -> error("unreachable")
                }
            }
        }
    }
    fun getEndSessionRequest(): EndSessionRequest{
        return EndSessionRequest
            .Builder(serviceConfiguration, Network.token, AuthConfig.LOGOUT_CALLBACK_URL.toUri())
            .apply {
                setAuthorizationServiceConfiguration(serviceConfiguration)
                setRedirectUri(AuthConfig.LOGOUT_CALLBACK_URL.toUri())
                setIdToken("1")
            }
            .build()
    }
    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }

    private object AuthConfig{
        const val AUTH_URI = "https://unsplash.com/oauth/authorize"
        const val TOKEN_URI = "https://unsplash.com/oauth/token"
        const val END_SESSION_URI = "https://unsplash.com/logout"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPE = "public read_user read_photos write_likes read_collections write_collections"

        const val CLIENT_ID = "jDZiEr2f0fviI8fBjJgqWYjitUYHfFCZuId1xsmxJDk"
        const val CLIENT_SECRET = "RcIs0drXfIub5D5jPnVDReN4MMT15rn6jrgbIU0RUIw"
        const val CALLBACK_URL = "aniskovtsev://aniskovtsev.unsplash/login"
        const val LOGOUT_CALLBACK_URL = "aniskovtsev://aniskovtsev.unsplash/logout"


    }
}