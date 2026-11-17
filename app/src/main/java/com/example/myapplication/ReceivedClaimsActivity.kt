package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast

class ReceivedClaimsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_claims)

        val grid = findViewById<GridView>(R.id.gridReceivedClaims)
        val emptyMessage = findViewById<TextView>(R.id.tvEmptyClaims)

        val userId = intent.getIntExtra("userId", -1)

        if (userId == -1) {
            finish()
            return
        }

        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(this@ReceivedClaimsActivity)

                val claims = db.claimDao().getClaimsForOwner(userId)

                val list = mutableListOf<Map<String, Any>>()

                for (claim in claims) {

                    val item = db.itemDao().getItemById(claim.itemId)

                    if (item != null) {
                        val claimData = mapOf(
                            "photo" to item.photo,
                            "itemName" to item.name,
                            "claimantName" to claim.claimantName,
                            "message" to claim.message,
                            "status" to claim.status,
                            "acceptData" to claim,
                            "rejectData" to claim
                        )

                        list.add(claimData)
                    }
                }
                list
            }

            if (list.isEmpty()) {
                emptyMessage.visibility = View.VISIBLE
                grid.visibility = View.GONE
            } else {
                emptyMessage.visibility = View.GONE
                grid.visibility = View.VISIBLE
            }
            val adapter = SimpleAdapter(
                this@ReceivedClaimsActivity,
                list,
                R.layout.item_claim,
                arrayOf("photo", "itemName", "claimantName", "message", "status","acceptData","rejectData"),
                intArrayOf(R.id.claimItemImage, R.id.claimItemName, R.id.claimantName, R.id.claimMessage, R.id.claimStatus,R.id.btnAccept,R.id.btnReject)
            )

            adapter.viewBinder = SimpleAdapter.ViewBinder{ view, value, _ ->
                when {
                    view is ImageView -> {
                        val path = value as String

                        if (path.isNotEmpty()) {
                            view.setImageBitmap(BitmapFactory.decodeFile(path))
                        }
                        true
                    }
                    view.id == R.id.claimStatus -> {
                        val statusView = view as TextView
                        val status = value as String
                        statusView.text = status

                        when (status) {
                            "PENDING" -> {
                                statusView.setTextColor("#8A3B12".toColorInt())
                                statusView.backgroundTintList = android.content.res.ColorStateList.valueOf("#FDE6D3".toColorInt())
                            }

                            "ACCEPTED" -> {
                                statusView.setTextColor("#1E5A3C".toColorInt())
                                statusView.backgroundTintList = android.content.res.ColorStateList.valueOf("#D8EFE3".toColorInt())
                            }

                            "REJECTED" -> {
                                statusView.setTextColor("#991B1B".toColorInt())
                                statusView.backgroundTintList = android.content.res.ColorStateList.valueOf("#FEE2E2".toColorInt())
                            }
                        }
                        true
                    }
                    view.id == R.id.claimantName -> {
                        (view as TextView).text = "Claimed by: $value"
                        true
                    }

                    view.id == R.id.btnAccept -> {
                        val claim = value as Claim
                        val actions = view.parent as View

                        if(claim.status == "PENDING"){
                            actions.visibility = View.VISIBLE
                        }else{
                            actions.visibility = View.GONE
                        }

                        (view as Button).setOnClickListener {
                            lifecycleScope.launch {
                                try {
                                    withContext(Dispatchers.IO) {
                                        val db = AppDatabase.getInstance(this@ReceivedClaimsActivity)
                                        // 1. Get other pending claims for the same item
                                        val otherPendingClaims = db.claimDao()
                                            .getClaimsForItem(claim.itemId)
                                            .filter { it.id != claim.id && it.status == "PENDING" }

                                        // 2. Accept the selected claim
                                        db.claimDao().updateClaimStatus(claim.id, "ACCEPTED")

                                        // 3. Reject the other pending claims
                                        db.claimDao().rejectOther(claim.itemId, claim.id)

                                        // 4. Notify users whose claims were automatically rejected
                                        for (otherClaim in otherPendingClaims) {
                                            val notification = NotificationEntity(
                                                userId = otherClaim.claimantUserId,
                                                title = "Claim Rejected",
                                                message = "Unfortunately, your claim for this item was rejected because another claim was accepted.",
                                                type = "CLAIM_REJECTED",
                                                itemId = otherClaim.itemId
                                            )

                                            db.notificationDao().insertNotification(notification)
                                        }
                                        // 5. Notify the user whose claim was accepted
                                        val acceptedNotification = NotificationEntity(
                                            userId = claim.claimantUserId,
                                            title = "Claim Accepted",
                                            message = "Good news! Your claim for this item has been accepted.",
                                            type = "CLAIM_ACCEPTED",
                                            itemId = claim.itemId
                                        )

                                        db.notificationDao().insertNotification(acceptedNotification)
                                    }
                                    Toast.makeText(this@ReceivedClaimsActivity, "Claim accepted successfully", Toast.LENGTH_SHORT).show()
                                    recreate()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@ReceivedClaimsActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        true
                    }

                    view.id == R.id.btnReject -> {
                        val claim = value as Claim

                        (view as Button).setOnClickListener {
                            lifecycleScope.launch {
                                try {
                                    withContext(Dispatchers.IO) {
                                        val db = AppDatabase.getInstance(this@ReceivedClaimsActivity)

                                        // 1. Update claim status
                                        db.claimDao().updateClaimStatus(claim.id, "REJECTED")

                                        // 2. Notify the claimant
                                        val notification = NotificationEntity(
                                            userId = claim.claimantUserId,
                                            title = "Claim Rejected",
                                            message = "Unfortunately, your claim for this item has been rejected.",
                                            type = "CLAIM_REJECTED",
                                            itemId = claim.itemId
                                        )

                                        db.notificationDao().insertNotification(notification)
                                    }

                                    Toast.makeText(
                                        this@ReceivedClaimsActivity, "Claim rejected", Toast.LENGTH_SHORT).show()

                                    recreate()
                                } catch (e: Exception) {
                                    Toast.makeText(this@ReceivedClaimsActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        true
                    }

                    else -> false
                }
            }

            grid.adapter = adapter
        }
    }
}