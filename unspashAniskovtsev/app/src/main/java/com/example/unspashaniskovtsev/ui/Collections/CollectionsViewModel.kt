package com.example.unspashaniskovtsev.ui.Collections

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unspashaniskovtsev.Models.Collections
import com.example.unspashaniskovtsev.Models.DetailPhoto
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.ui.Home.Detail.DetailPhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val repository: CollectionsRepository
): ViewModel() {


    val collectionsFlow = repository.getCollections()
        .cachedIn(viewModelScope)



    fun pressLike(collection: Collections) = viewModelScope.launch {
        try {
            repository.pressLike(collection)
        } catch (e:Exception) {
            Timber.d(e)
        }
    }

}