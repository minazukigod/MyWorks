package com.example.unspashaniskovtsev.data.db.Room

import android.content.Context
import androidx.room.Room

object Database {
    lateinit var instance: AniskovtsevDatabase
        private set

    fun initDatabase(context: Context) {
        instance = Room.databaseBuilder(
            context,
            AniskovtsevDatabase::class.java,
            AniskovtsevDatabase.DATABASE_NAME
        )
            .build()
    }


}