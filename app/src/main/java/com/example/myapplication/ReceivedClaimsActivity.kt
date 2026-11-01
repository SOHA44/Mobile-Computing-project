package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
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

                claims.mapNotNull { claim ->
                    val item = db.itemDao().getItemById(claim.itemId)
                    if (item != null) {
                        mapOf(
                            "photo" to item.photo,
                            "itemName" to item.name,
                            "claimantName" to claim.claimantName,
                            "message" to claim.message,
                            "status" to claim.status
                        )
                    } else {
                        null
                    }
                }
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
                arrayOf("photo", "itemName", "claimantName", "message", "status"),
                intArrayOf(R.id.claimItemImage, R.id.claimItemName, R.id.claimantName, R.id.claimMessage, R.id.claimStatus)
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
                    else -> false
                }
            }

            grid.adapter = adapter
        }
    }
}