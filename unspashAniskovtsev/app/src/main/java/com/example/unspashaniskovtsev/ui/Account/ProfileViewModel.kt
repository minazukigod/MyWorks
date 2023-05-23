package com.example.unspashaniskovtsev.ui.Account

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.unspashaniskovtsev.Models.CurrentUser
import com.example.unspashaniskovtsev.Models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
): ViewModel() {


    private val userFlow = MutableStateFlow(CurrentUser("", "", "", "", "", "", ""))
    val userFlowGetter = userFlow.asStateFlow()

    private val imageFlow = MutableStateFlow("")
    val imageFlowGetter = imageFlow.asStateFlow()

    private val loadingFlow = MutableStateFlow(false)
    val loadingFlowGetter = loadingFlow.asStateFlow()

    private val toastFlow = MutableStateFlow("")
    val toastFlowGetter = toastFlow.asStateFlow()


    private val username = MutableLiveData("")

    val photoFlow = username.asFlow()
        .flatMapLatest {
            repository.getProfilePhotosPager(it).flow
        }
        .cachedIn(viewModelScope)

    fun setUsername(username: String){
        this.username.value = username
    }

    fun pressLike(photo: Photo){
        viewModelScope.launch {
            try {
                repository.pressLike(photo)
            }catch (e:Exception){
                Log.d("PressLikeProfile", e.message.toString())
            }
        }
    }



    fun getInfoAboutCurrentUser(){
        viewModelScope.launch {
            loadingFlow.value = true
            try {
                val rawUser = repository.getInfoAboutCurrentUser()
                userFlow.value = rawUser
                val imageLink = repository.getImage(rawUser.username).profile_image.small
                imageFlow.value = imageLink
                loadingFlow.value = false
            } catch (e: HttpException){
                loadingFlow.value = false
                toastFlow.value = e.message.toString()
            }

        }
    }
}