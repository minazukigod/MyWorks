package com.example.unspashaniskovtsev.ui.Home.Detail

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.unspashaniskovtsev.Models.DetailPhoto
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.data.db.Models.PhotoEntity
import com.example.unspashaniskovtsev.data.db.Room.Database
import com.example.unspashaniskovtsev.data.db.Room.PhotoDao
import com.example.unspashaniskovtsev.utils.haveQ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailPhotoRepository @Inject constructor(
    private val context: Application,
) {

    private val dao = Database.instance.photoDao()

    suspend fun getDetailInfoAboutPhoto(photoId: String): DetailPhoto{
        return Network.unsplashApi.getDeatilPhoto(photoId)
    }


    fun downloadPhoto(url: String) {
        val workData = workDataOf(
            DownloadWorker.DOWNLOAD_WORK_KEY to url
        )



        val workerRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(workData)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                DownloadWorker.DOWNLOAD_WORK_ID,
                ExistingWorkPolicy.KEEP,
                workerRequest
            )


    }

    suspend fun pressLike(photo: Photo) {
        if (photo.liked_by_user) {
            Network.unsplashApi.unlikePhoto(photo.id)
        } else {
            Network.unsplashApi.likePhoto(photo.id)
        }
        dao.saveChanges(
            PhotoEntity(
                photo.id,
                photo.likes,
                !photo.liked_by_user,
                photo.urls.full,
                photo.user.username,
                photo.user.id,
                photo.user.profile_image.small,
                photo.blur_hash
            )
        )
    }

}
