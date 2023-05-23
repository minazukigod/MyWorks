package com.example.unspashaniskovtsev.ui.Home

import androidx.paging.*
import com.example.unspashaniskovtsev.Models.*
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.data.db.Models.PhotoEntity
import com.example.unspashaniskovtsev.data.db.Room.PhotoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val dao: PhotoDao,
    private val retrofit: Retrofit
){


    @OptIn(ExperimentalPagingApi::class)
    fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(20, enablePlaceholders = false),
            remoteMediator = PhotosRemoteMediator(dao),
            pagingSourceFactory = {dao.getPagingSource()}
        ).flow
            .map { value: PagingData<PhotoEntity> ->
                value.map {
                    Photo(it.id, it.likes, it.liked_by_user, User(it.userID, it.username, ProfileImage(it.userImageLink), null), Urls(it.url), it.blurHash)
                }
            }
    }

    suspend fun pressLike(photo: Photo){
        var isLiked = false
        if (photo.liked_by_user){
            Network.unsplashApi.unlikePhoto(photo.id)
        } else {
            isLiked = true
            Network.unsplashApi.likePhoto(photo.id)
        }

        var likes = photo.likes
        if (isLiked){
            likes+=1
        } else {
            if (likes != 0){
                likes-=1
            }
        }

//        val editedPhotoEntity = PhotoEntity(photo.id, photo.likes, photo.liked_by_user, photo.urls.full, photo.user.username, photo.user.id, photo.user.profile_image.small, photo.blur_hash)
//            .copy(liked_by_user = !photo.liked_by_user, likes = likes)
//        dao.saveChanges(editedPhotoEntity)
    }

    suspend fun getTopPhoto() : List<PhotoEntity>{
        return dao.getTopPhoto()
    }





}