
package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "claims")
data class Claim(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    //val itemId: Int,
    val claimantUserId: Int,
    val claimantName: String,
    val message: String,
    val status: String = "PENDING"
)
