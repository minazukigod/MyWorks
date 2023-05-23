package com.example.unspashaniskovtsev.ui.Collections

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.unspashaniskovtsev.Models.Collections
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.data.db.Models.CollectionsEntity
import com.example.unspashaniskovtsev.data.db.Room.CollectionsDao
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class CollectionsRemoteMediator(
    private val collectionsDao: CollectionsDao
):RemoteMediator<Int, CollectionsEntity>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CollectionsEntity>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?: return MediatorResult.Success(true)
        val limit = state.config.pageSize

        return try {
            val collections = Network.unsplashApi.getCollections(limit, pageIndex)

            if (loadType == LoadType.REFRESH){
                collectionsDao.refresh(convert(collections))
            } else{
                collectionsDao.save(convert(collections))
            }
            MediatorResult.Success(collections.size < limit)
        } catch (e: Exception){
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

        private fun convert(collections: List<Collections>): List<CollectionsEntity> {

            val convertedList = mutableListOf<CollectionsEntity>()
            collections.forEach {
                convertedList.add(
                    CollectionsEntity(
                        it.id,
                        it.cover_photo.likes,
                        it.cover_photo.liked_by_user,
                        it.cover_photo.blur_hash,
                        it.cover_photo.user.id,
                        it.total_photos,
                        it.cover_photo.user.profile_image.small,
                        it.cover_photo.urls.full,
                        it.cover_photo.user.username,
                        it.cover_photo.id,
                        it.title
                    )
                )
            }
            return convertedList
        }
    }

