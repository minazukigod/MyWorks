package com.example.unspashaniskovtsev

import android.app.Application
import com.example.unspashaniskovtsev.data.db.Room.Database
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AniskovtsevApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Database.initDatabase(this)
    }
}