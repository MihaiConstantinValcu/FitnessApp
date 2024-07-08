package com.example.fitnessapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserModel (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ARG_ID)
    var id: Int = 0,
    val username : String,
    val email : String,
    val city: String,
    val sport: String
) {
    companion object {
        const val ARG_ID = "id"
    }
}