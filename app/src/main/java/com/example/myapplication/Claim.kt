
package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "claims",
        foreignKeys = [
            ForeignKey(entity = Items::class , parentColumns = ["id"], childColumns = ["itemId"], onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["claimantUserId"], onDelete = ForeignKey.CASCADE)
        ],
    )

data class Claim(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: Int,
    val claimantUserId: Int,
    val claimantName: String,
    val message: String,
    val status: String = "PENDING"
)
