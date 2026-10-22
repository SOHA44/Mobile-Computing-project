package com.example.myapplication

interface user_dao {
    @Insert
    suspend fun signUp(user: User)
    @Query("SELECT * FROM users WHERE email = :email && password = :password")
    suspend fun signIn(email: String)
    @Update
    suspend fun updateUser(user: User)
    @Delete
    suspend fun deleteUser(user: User)
}