package com.example.unspashaniskovtsev.di

import android.app.Application
import androidx.room.Room
import com.example.unspashaniskovtsev.data.db.Room.AniskovtsevDatabase
import com.example.unspashaniskovtsev.data.db.Room.CollectionsDao
import com.example.unspashaniskovtsev.data.db.Room.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DatabaseModule {
    @Provides
    fun providesCollectionDao(db: AniskovtsevDatabase): CollectionsDao{
        return db.collectionsDao()
    }
    @Provides
    fun providesPhotoDao(db: AniskovtsevDatabase): PhotoDao{
        return db.photoDao()
    }
    @Provides
    fun providesDatabase(application: Application): AniskovtsevDatabase{
        return Room.databaseBuilder(
            application,
            AniskovtsevDatabase::class.java,
            AniskovtsevDatabase.DATABASE_NAME
        )
            .build()
    }
}