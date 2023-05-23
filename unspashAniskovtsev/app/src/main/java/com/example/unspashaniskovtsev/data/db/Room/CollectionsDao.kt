package com.example.unspashaniskovtsev.data.db.Room

import androidx.paging.PagingSource
import androidx.room.*
import com.example.unspashaniskovtsev.data.db.Models.CollectionsContract
import com.example.unspashaniskovtsev.data.db.Models.CollectionsEntity


@Dao
interface CollectionsDao {

    @Query(value = "SELECT * FROM ${CollectionsContract.TABLE_NAME_COLLECTIONS}")
    fun getPagingSource(): PagingSource<Int, CollectionsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(photos: List<CollectionsEntity>)

    @Query(value = "DELETE FROM ${CollectionsContract.TABLE_NAME_COLLECTIONS}")
    suspend fun clear()

    @Transaction
    suspend fun refresh(collections: List<CollectionsEntity>){
        clear()
        save(collections)
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChanges(collectionEntity: CollectionsEntity)
}
