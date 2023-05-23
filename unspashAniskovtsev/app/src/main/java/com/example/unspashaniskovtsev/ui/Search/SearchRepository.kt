package com.example.unspashaniskovtsev.ui.Search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.data.Network.Network
import javax.inject.Inject

class SearchRepository @Inject constructor() {


    fun getSearchPhotosPager(query: String): Pager<Int, Photo> {
        val pagingSource = SearchSource(query)
        return Pager(
            PagingConfig(pageSize = 50, initialLoadSize = 50, prefetchDistance = 35, enablePlaceholders = false)
        ){
            pagingSource
        }
    }


    suspend fun pressLike(photo: Photo){
        if (photo.liked_by_user){
            Network.unsplashApi.unlikePhoto(photo.id)
        } else {
            Network.unsplashApi.likePhoto(photo.id)
        }

    }
}