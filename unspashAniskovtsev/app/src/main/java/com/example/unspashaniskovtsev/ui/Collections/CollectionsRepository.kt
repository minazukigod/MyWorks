package com.example.unspashaniskovtsev.ui.Collections

import androidx.paging.*
import com.example.unspashaniskovtsev.Models.*
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.data.db.Models.CollectionsEntity
import com.example.unspashaniskovtsev.data.db.Room.CollectionsDao
import com.example.unspashaniskovtsev.data.db.Room.Database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import javax.inject.Inject

class CollectionsRepository @Inject constructor(
    private val dao : CollectionsDao,
    private val retrofit: Retrofit
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getCollections(): Flow<PagingData<Collections>> {
        return Pager(
            config = PagingConfig(20),
            remoteMediator = CollectionsRemoteMediator(dao),
            pagingSourceFactory = {dao.getPagingSource()}
        ).flow
            .map { pagingData ->
                pagingData.map {
                    Collections(it.collectionId, it.total_photos, Photo(it.imageId, it.likes, it.liked_by_user, User(it.userId, it.username, ProfileImage(it.userImageLink), null), Urls(it.url), it.blur_hash), it.title)
                }
            }
    }

    suspend fun pressLike(collections: Collections){
        var isLiked = false
        if (collections.cover_photo.liked_by_user){
            Network.unsplashApi.unlikePhoto(collections.cover_photo.id)
        } else {
            isLiked = true
            Network.unsplashApi.likePhoto(collections.cover_photo.id)
        }
        var likes = collections.cover_photo.likes
        if (isLiked){
            likes+=1
        } else {
            if (likes != 0){
                likes-=1
            }
        }

//        val editedCollectionEntity = CollectionsEntity(collections.id, collections.cover_photo.likes, collections.cover_photo.liked_by_user, collections.cover_photo.blur_hash, collections.cover_photo.user.id, collections.total_photos, collections.cover_photo.user.profile_image.small, collections.cover_photo.urls.full, collections.cover_photo.user.username, collections.cover_photo.id, collections.title)
//            .copy(liked_by_user = !collections.cover_photo.liked_by_user, likes = likes)
//        dao.saveChanges(editedCollectionEntity)


    }

}