package com.example.unspashaniskovtsev.ui.Account

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.unspashaniskovtsev.Models.CurrentUser
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.Models.User
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.data.db.Models.PhotoEntity
import com.example.unspashaniskovtsev.data.db.Room.Database
import com.example.unspashaniskovtsev.data.db.Room.PhotoDao
import com.example.unspashaniskovtsev.ui.Search.SearchSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val dao: PhotoDao
) {



    suspend fun getInfoAboutCurrentUser():CurrentUser{
        return withContext(Dispatchers.IO){
            Network.unsplashApi.getInfoAboutCurrentUser()
        }
    }

    suspend fun getImage(user: String): User{
        return withContext(Dispatchers.IO){
            Network.unsplashApi.getImage(user)
        }
    }

    fun getProfilePhotosPager(username: String): Pager<Int, Photo> {
        val pagingSource = ProfileSource(username)
        return Pager(
            PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 10, enablePlaceholders = false)
        ){
            pagingSource
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

        val editedPhotoEntity = PhotoEntity(photo.id, photo.likes, photo.liked_by_user, photo.urls.full, photo.user.username, photo.user.id, photo.user.profile_image.small, photo.blur_hash)
            .copy(liked_by_user = !photo.liked_by_user, likes = likes)
        dao.saveChanges(editedPhotoEntity)


    }
}