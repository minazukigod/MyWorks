package com.example.unspashaniskovtsev.ui.Search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unspashaniskovtsev.Models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
): ViewModel(){

    private val search = MutableLiveData("")



    val loadingFlow = MutableStateFlow(false)
    val loadingFlowGetter = loadingFlow.asStateFlow()

    val originPhotoFlow = search.asFlow()
        .debounce(700)
        .flatMapLatest {
            if (it.isNotEmpty()) loadingFlow.value = true
            repository.getSearchPhotosPager(it).flow
        }
        .catch {
            loadingFlow.value = false
        }
        .onEach {
            delay(1000)
            loadingFlow.value = false
        }
        .cachedIn(viewModelScope)




    fun setSearch(query: String){
        this.search.value = query
    }



    fun pressLike(photo: Photo){
        viewModelScope.launch {
            try {
                repository.pressLike(photo)
            }catch (e:Exception){
                Log.d("SearchPressLike", e.message.toString())
            }
        }
    }

}