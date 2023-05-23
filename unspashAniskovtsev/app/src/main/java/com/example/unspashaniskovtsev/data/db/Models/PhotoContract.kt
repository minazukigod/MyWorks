package com.example.unspashaniskovtsev.data.db.Models

import com.example.unspashaniskovtsev.Models.Urls


object PhotoContract {

    const val TABLE_NAME_PHOTO = "photos"

    object Columns{
        const val ID = "id"
        const val URLS = "urls"
        const val USER_ID = "userID"
        const val LIKED_BY_USER = "liked_by_user"
        const val LIKES = "likes"
        const val USER_IMAGE_LINK = "user_link"
        const val USERNAME = "username"
        const val BLURHASH = "blurhash"
    }
}
