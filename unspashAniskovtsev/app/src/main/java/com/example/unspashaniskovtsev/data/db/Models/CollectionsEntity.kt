package com.example.unspashaniskovtsev.data.db.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CollectionsContract.TABLE_NAME_COLLECTIONS)
data class CollectionsEntity(
    @PrimaryKey
    @ColumnInfo(name = CollectionsContract.Columns.COLLECTION_ID)
    val collectionId: String,
    @ColumnInfo(name = CollectionsContract.Columns.LIKES)
    val likes: Int,
    @ColumnInfo(name = CollectionsContract.Columns.LIKED_BY_USER)
    val liked_by_user: Boolean,
    @ColumnInfo(name = CollectionsContract.Columns.BLURHASH)
    val blur_hash: String?,
    @ColumnInfo(name = CollectionsContract.Columns.USER_ID)
    val userId: String,
    @ColumnInfo(name = CollectionsContract.Columns.TOTAL_PHOTOS)
    val total_photos: Int,
    @ColumnInfo(name = CollectionsContract.Columns.USER_IMAGE_LINK)
    val userImageLink: String,
    @ColumnInfo(name = CollectionsContract.Columns.URLS)
    val url: String,
    @ColumnInfo(name = CollectionsContract.Columns.USERNAME)
    val username: String,
    @ColumnInfo(name = CollectionsContract.Columns.IMAGE_ID)
    val imageId: String,
    @ColumnInfo(name = CollectionsContract.Columns.TITLE)
    val title: String


    )

//Photo
//val id: String,
//val likes: Int,
//val liked_by_user: Boolean,
//val user: User,
//val urls: Urls,
//val blur_hash: String

//Collection
//val id: Int,
//val total_photos: Int,
//val cover_photo: Photo,
