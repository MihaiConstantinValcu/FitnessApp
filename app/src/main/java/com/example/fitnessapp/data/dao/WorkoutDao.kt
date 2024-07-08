package com.example.fitnessapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitnessapp.models.WorkoutModel

@Dao
public interface WorkoutDao {
    @Insert
    fun insert(workout: WorkoutModel)

    @Update
    fun update(workout: WorkoutModel)

    @Query("SELECT * FROM workout WHERE userEmail = :userEmail")
    fun getAllWorkoutsByUserEmail(userEmail: String): List<WorkoutModel>
}