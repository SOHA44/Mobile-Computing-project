package com.example.myapplication
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface  user_dao {
    @Insert
    suspend fun signUp(user: User): Long
    @Query("SELECT * FROM users WHERE email = :email And password = :password")
    suspend fun signIn(email: String, password: String): User?
    @Query("SELECT * FROM users WHERE email = :email" )
    suspend fun validate(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id" )
    suspend fun getUser(id: Int): User?
    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET name = :name WHERE id = :id")
    suspend fun updateName(id: Int, name: String)

    @Query("UPDATE users SET phoneNumber = :phoneNumber WHERE id = :id")
    suspend fun updatePhoneNumber(id: Int, phoneNumber: String)

    @Query("UPDATE users SET password = :password WHERE id = :id")
    suspend fun updatePassword(id: Int, password: String)

    @Query("UPDATE users SET image = :image WHERE id = :id")
    suspend fun updateImage(id: Int, image: String)

    @Delete
    suspend fun deleteUser(user: User)
}