package com.example.unspashaniskovtsev.data.db.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unspashaniskovtsev.data.db.Models.CollectionsEntity
import com.example.unspashaniskovtsev.data.db.Models.PhotoEntity

@Database(entities = [CollectionsEntity::class, PhotoEntity::class], version = AniskovtsevDatabase.DATABASE_VERSION)
abstract class AniskovtsevDatabase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    abstract fun collectionsDao(): CollectionsDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "aniskovtsevDatabase"
    }
}