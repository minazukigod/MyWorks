package com.example.unspashaniskovtsev.ui.Collections.Detail

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.ui.Search.SearchSource
import javax.inject.Inject

class DetailCollectionsRepository @Inject constructor() {


    fun getDetailsCollectionPhotosPager(id: String): Pager<Int, Photo> {
        val pagingSource = DetailCollectionsSource(id)
        return Pager(
            PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 10, enablePlaceholders = false)
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