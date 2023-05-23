package com.example.unspashaniskovtsev.ui.Home.Detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unspashaniskovtsev.Models.DetailPhoto
import com.example.unspashaniskovtsev.Models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailPhotoViewModel @Inject constructor(
    private val repository : DetailPhotoRepository
): ViewModel() {


    val loadingFlow = MutableStateFlow(false)
    lateinit var detailPhotoFlow: Flow<DetailPhoto>


    fun getDetailPhoto(photoID: String){
        viewModelScope.launch {
            try {
                loadingFlow.value = true
                detailPhotoFlow =  flow{
                    try {
                        emit(repository.getDetailInfoAboutPhoto(photoID))
                    } catch (e: Exception){
                        Timber.d(e)
                    }
                }
                loadingFlow.value = false
            } catch (e: Exception){
                Timber.d(e)
                loadingFlow.value = false
            }
        }
    }

    fun downloadImage(url: String){
        repository.downloadPhoto(url)

    }

    fun pressLike(photo: Photo){
        viewModelScope.launch {
            repository.pressLike(photo)
        }
    }
}