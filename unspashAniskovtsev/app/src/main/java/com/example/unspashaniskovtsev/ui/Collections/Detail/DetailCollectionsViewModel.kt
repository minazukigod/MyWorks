package com.example.unspashaniskovtsev.ui.Collections.Detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.unspashaniskovtsev.Models.DetailPhoto
import com.example.unspashaniskovtsev.Models.ListPhotos
import com.example.unspashaniskovtsev.Models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailCollectionsViewModel @Inject constructor(
    private val repository: DetailCollectionsRepository
): ViewModel() {

    private val id = MutableLiveData("")

    val photosFLow = id.asFlow()
        .flatMapLatest {
            repository.getDetailsCollectionPhotosPager(it).flow
        }
        .cachedIn(viewModelScope)

    fun setId(id: String){
        this.id.value = id
    }

    fun pressLike(photo: Photo){
        viewModelScope.launch {
            try {
                repository.pressLike(photo)
            }catch (e:Exception){
                Log.d("DetailCollectionLike", e.message.toString())
            }
        }
    }


}