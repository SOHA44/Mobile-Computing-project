package com.example.myapplication
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface  user_dao {
    @Insert
    suspend fun signUp(user: User)
    @Query("SELECT * FROM users WHERE email = :email And password = :password")
    suspend fun signIn(email: String, password: String): User?
    @Query("SELECT * FROM users WHERE email = :email" )
    suspend fun validate(email: String): User?
    @Update
    suspend fun updateUser(user: User)
    @Delete
    suspend fun deleteUser(user: User)
}