package com.example.unspashaniskovtsev.data.db.Room

import androidx.paging.PagingSource
import androidx.room.*
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.data.db.Models.PhotoContract
import com.example.unspashaniskovtsev.data.db.Models.PhotoEntity

@Dao
interface PhotoDao {

    @Query(value = "SELECT * FROM ${PhotoContract.TABLE_NAME_PHOTO}")
    fun getPagingSource(): PagingSource<Int, PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(photos: List<PhotoEntity>)

    @Query(value = "DELETE FROM ${PhotoContract.TABLE_NAME_PHOTO}")
    suspend fun clear()

    @Transaction
    suspend fun refresh(photos: List<PhotoEntity>){
        clear()
        save(photos)
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChanges(photoEntity: PhotoEntity)

    @Query(value = "SELECT * FROM ${PhotoContract.TABLE_NAME_PHOTO} ORDER BY ${PhotoContract.Columns.LIKES} DESC")
    suspend fun getTopPhoto(): List<PhotoEntity>


}