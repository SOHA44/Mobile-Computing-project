package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.net.URL
import java.sql.Date

@Entity (tableName = "items",
         foreignKeys = [ForeignKey
             (entity = User::class,
              parentColumns = ["id"],
              childColumns = ["ownerUserId"],
              onDelete = ForeignKey.CASCADE
                     )]
)

data class Items(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val name: String,
    val description : String,
    val photo: String="",
    val date: String,
    val location : String,
    val contactInfo: String,
    val ownerUserId: Int,
    val type: String
)
