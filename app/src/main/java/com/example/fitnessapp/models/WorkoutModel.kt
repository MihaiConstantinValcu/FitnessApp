package com.example.fitnessapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout")
class WorkoutModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ARG_ID)
    val id: Int = 0,
    val date: LocalDate,
    val duration: Int,
    val userEmail: String
) {
    companion object {
        const val ARG_ID = "id"
    }
}