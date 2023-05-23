package com.example.unspashaniskovtsev.data.db.Models

object CollectionsContract {

    const val TABLE_NAME_COLLECTIONS = "collections"

    object Columns{
        const val COLLECTION_ID = "collectionId"
        const val URLS = "urls"
        const val USER_ID = "userID"
        const val LIKED_BY_USER = "liked_by_user"
        const val LIKES = "likes"
        const val USER_IMAGE_LINK = "user_link"
        const val USERNAME = "username"
        const val BLURHASH = "blurhash"
        const val IMAGE_ID = "imageId"
        const val TOTAL_PHOTOS = "totalPhotos"
        const val TITLE = "title"

    }
}

//val collectionId: Int,
//val likes: Int,
//val liked_by_user: Boolean,
//val blur_hash: String,
//val userId: String,
//val total_photos: Int,