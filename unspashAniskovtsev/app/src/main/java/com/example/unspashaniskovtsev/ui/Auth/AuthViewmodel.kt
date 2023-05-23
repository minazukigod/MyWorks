package com.example.unspashaniskovtsev.ui.Auth

import android.app.Application
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unspashaniskovtsev.data.Auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository
    ): AndroidViewModel(application) {

    private val authService = AuthorizationService(getApplication())

    private val openAuthPageEventChannel = Channel<Intent>(Channel.BUFFERED)
    private val authSuccessEventChannel = Channel<Unit>(Channel.BUFFERED)


    private val loadingMutableStateFlow = MutableStateFlow(false)

    val openAuthPageFlow: Flow<Intent>
    get() = openAuthPageEventChannel.receiveAsFlow()

    val authSuccessFlow: Flow<Unit>
        get() = authSuccessEventChannel.receiveAsFlow()

    fun openLoginPage(){
        val customTabsIntent = CustomTabsIntent.Builder().build()

        val authRequest = authRepository.getAuthRequest()

        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )
        openAuthPageEventChannel.trySendBlocking(openAuthPageIntent)
    }


    fun onAuthCodeReceived(tokenExchangeRequest: TokenRequest) {
        viewModelScope.launch {
            loadingMutableStateFlow.value = true
            runCatching {
                authRepository.perfromTokenRequest(
                    authService, tokenExchangeRequest
                )
            }.onSuccess {
                loadingMutableStateFlow.value = false
                authSuccessEventChannel.send(Unit)
            }.onFailure {
                loadingMutableStateFlow.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }

}