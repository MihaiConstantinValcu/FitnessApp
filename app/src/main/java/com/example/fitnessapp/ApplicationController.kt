package com.example.fitnessapp

import android.app.Application
import androidx.room.Room
import com.example.fitnessapp.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationController : Application() {

    public lateinit var database: AppDatabase

    companion object {
        var instance: ApplicationController? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "fitness-database"
        ).allowMainThreadQueries().build()
    }
}