package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemsDao{
    @Insert
    suspend fun insertItem(item: Items)
    @Query("SELECT * FROM items")
    suspend fun getAllItems (): List<Items>
    @Update
    suspend fun updateItem(item: Items)
    @Delete
    suspend fun deleteItem(item: Items)
}
