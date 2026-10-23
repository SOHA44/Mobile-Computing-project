
package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface claim_dao {

    @Insert
    suspend fun insertClaim(claim: Claim)

    @Query("SELECT * FROM claims ORDER BY id DESC")
    suspend fun getAllClaims(): List<Claim>

    @Query("SELECT * FROM claims WHERE status = :status")
    suspend fun getClaimsByStatus(status: String): List<Claim>

    @Query("SELECT * FROM claims WHERE itemId = :itemId")
    suspend fun getClaimsForItem(itemId: Int): List<Claim>

    @Query("SELECT * FROM claims WHERE claimantUserId = :userId")
    suspend fun getClaimsByUser(userId: Int): List<Claim>

    @Query("SELECT * FROM claims WHERE id = :claimId")
    suspend fun getClaimById(claimId: Int): Claim?

    @Update
    suspend fun updateClaim(claim: Claim)

    @Query("UPDATE claims SET status = :status WHERE id = :claimId")
    suspend fun updateClaimStatus(claimId: Int, status: String)

    @Delete
    suspend fun deleteClaim(claim: Claim)
}
