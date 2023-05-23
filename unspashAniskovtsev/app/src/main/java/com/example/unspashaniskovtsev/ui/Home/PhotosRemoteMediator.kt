package com.example.unspashaniskovtsev.ui.Home

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.data.db.Models.PhotoEntity
import com.example.unspashaniskovtsev.data.db.Room.PhotoDao
import com.example.unspashaniskovtsev.utils.initRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMediator(
    private val photoDao: PhotoDao
) : RemoteMediator<Int, PhotoEntity>() {


    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?: return MediatorResult.Success(true)

        val limit = state.config.pageSize
        initRetrofit()
        return try {
            val photos = Network.unsplashApi.getListOfPhotos(limit, pageIndex)
            if (loadType == LoadType.REFRESH){
                photoDao.refresh(convert(photos))
            } else{
                photoDao.save(convert(photos))
            }
            MediatorResult.Success(photos.size < limit)
        } catch (e: Exception) {
            Log.d("Mediator", e.message.toString())
            return MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    private fun convert(photos: List<Photo>): List<PhotoEntity> {

        val convertedList = mutableListOf<PhotoEntity>()
        photos.forEach {
            convertedList.add(
                PhotoEntity(
                    it.id,
                    it.likes,
                    it.liked_by_user,
                    it.urls.full,
                    it.user.username,
                    it.user.id,
                    it.user.profile_image.small,
                    it.blur_hash
                )
            )
        }
        return convertedList
    }
}