package com.example.unspashaniskovtsev.ui.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unspashaniskovtsev.Models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
): ViewModel() {

    val photosFlow = repository.getPhotos()
        .cachedIn(viewModelScope)

     val topPhotoFlow = flow {
        val photo = repository.getTopPhoto()
        emit(photo)
    }



    fun pressLike(photo: Photo) = viewModelScope.launch {
        try {
            repository.pressLike(photo)
        } catch (e:Exception) {
            Timber.d(e)
        }
    }


}