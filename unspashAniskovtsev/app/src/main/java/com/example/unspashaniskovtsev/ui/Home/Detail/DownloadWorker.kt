package com.example.unspashaniskovtsev.ui.Home.Detail

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.utils.haveQ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class DownloadWorker(
    val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val urlToDownload = inputData.getString(DOWNLOAD_WORK_KEY)
        return try {
            if (urlToDownload != null) {
                saveImage(Random.nextLong().toString(), urlToDownload)
            }
            Result.success()
        } catch (e: Exception){
            Result.retry()
        }

    }

    private suspend fun saveImage(name: String, url: String){
        withContext(Dispatchers.IO){
            val imageUri = saveImageDetails(name)
            downloadImage(url, imageUri)
            makeImageVisible(imageUri)
        }
    }

    private fun saveImageDetails(name: String): Uri {
        val volume = if (haveQ()){
            MediaStore.VOLUME_EXTERNAL_PRIMARY
        } else {
            MediaStore.VOLUME_EXTERNAL
        }

        val imageUri = MediaStore.Images.Media.getContentUri(volume)
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (haveQ()){
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        return context.contentResolver.insert(imageUri, imageDetails)!!
    }

    private suspend fun downloadImage(url: String, uri: Uri){
        context.contentResolver.openOutputStream(uri).use { outputStream ->
            Network.unsplashApi
                .getFile(url)
                .byteStream()
                .use { inputStream ->
                    inputStream.copyTo(outputStream!!)

                }
        }

    }



    private fun makeImageVisible(imageUri: Uri){
        if (haveQ().not()) return
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.IS_PENDING, 0)
        }
        context.contentResolver.update(imageUri, imageDetails, null, null)
    }
    companion object {
        const val DOWNLOAD_WORK_KEY = "url"
        const val DOWNLOAD_WORK_ID = "workDownload"

    }
}