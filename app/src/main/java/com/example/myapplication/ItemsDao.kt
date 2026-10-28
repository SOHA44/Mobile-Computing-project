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

    @Query("SELECT * FROM items WHERE type='found'")
    suspend fun getAllFoundItems (): List<Items>

    @Query("SELECT * FROM items WHERE type='lost'")
    suspend fun getAllLostItems (): List<Items>

    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): Items?

    @Query("SELECT * FROM items WHERE ownerUserId = :userId")
    suspend fun getItemsByOwner(userId: Int): List<Items>

    @Update
    suspend fun updateItem(item: Items)
    @Delete
    suspend fun deleteItem(item: Items)
}
