package com.example.fitnessapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitnessapp.data.dao.UserDao
import com.example.fitnessapp.data.dao.WorkoutDao
import com.example.fitnessapp.models.UserModel
import com.example.fitnessapp.models.WorkoutModel

@Database(entities = [UserModel::class, WorkoutModel::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
}