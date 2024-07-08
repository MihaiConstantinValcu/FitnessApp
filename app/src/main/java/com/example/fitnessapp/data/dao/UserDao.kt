package com.example.fitnessapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fitnessapp.models.UserModel
import com.firebase.ui.auth.data.model.User

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(model: UserModel)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun  updateUser(model: UserModel)

    @Query("SELECT * from user WHERE email = :email")
    fun getUser(email: String): UserModel?

    @Query("SELECT COUNT(*) from user")
    fun getUserCount(): Int

}