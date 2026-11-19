package com.example.myapplication

import androidx.compose.ui.text.input.TransformedText
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemsDao{
    @Insert
    suspend fun insertItem(item: Items): Long
    @Query("SELECT * FROM items")
    suspend fun getAllItems (): List<Items>

    @Query("SELECT * FROM items WHERE type='found'")
    suspend fun getAllFoundItems (): List<Items>

    @Query("SELECT * FROM items WHERE type='lost'")
    suspend fun getAllLostItems (): List<Items>

    @Query("SELECT * FROM items WHERE type = 'lost' AND ownerUserId = :userId")
    suspend fun getMyLostItems(userId: Int): List<Items>

    @Query("SELECT * FROM items WHERE type = 'found' AND ownerUserId = :userId")
    suspend fun getMyFoundItems(userId: Int): List<Items>

    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): Items?

    @Query("SELECT * FROM items WHERE ownerUserId = :userId")
    suspend fun getItemsByOwner(userId: Int): List<Items>

    @Query("SELECT * FROM items WHERE name LIKE '%' || :searchText || '%' OR description LIKE '%' || :searchText || '%' OR location LIKE '%' || :searchText || '%'")
    suspend fun searchItems(searchText: String): List<Items>

    @Update
    suspend fun updateItem(item: Items)
    @Delete
    suspend fun deleteItem(item: Items)
}
