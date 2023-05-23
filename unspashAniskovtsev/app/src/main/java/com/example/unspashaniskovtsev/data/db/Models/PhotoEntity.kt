package com.example.unspashaniskovtsev.data.db.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PhotoContract.TABLE_NAME_PHOTO)
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = PhotoContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = PhotoContract.Columns.LIKES)
    val likes: Int,
    @ColumnInfo(name = PhotoContract.Columns.LIKED_BY_USER)
    val liked_by_user: Boolean,
    @ColumnInfo(name = PhotoContract.Columns.URLS)
    val url: String,
    @ColumnInfo(name = PhotoContract.Columns.USERNAME)
    val username: String,
    @ColumnInfo(name = PhotoContract.Columns.USER_ID)
    val userID: String,
    @ColumnInfo(name = PhotoContract.Columns.USER_IMAGE_LINK)
    val userImageLink: String,
    @ColumnInfo(name = PhotoContract.Columns.BLURHASH)
    val blurHash: String?
)
