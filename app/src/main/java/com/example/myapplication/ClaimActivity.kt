package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClaimActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim)

        val tvClaimItem = findViewById<TextView>(R.id.tvClaimItem)
        val tvClaimantName = findViewById<TextView>(R.id.tvClaimantName)
        val messageInput = findViewById<EditText>(R.id.etClaimMessage)
        val submitButton = findViewById<Button>(R.id.btnSubmitClaim)

        val itemId = intent.getIntExtra("ITEM_ID", -1)
        val userId = intent.getIntExtra("USER_ID", -1)
        val userName = intent.getStringExtra("USER_NAME")
        if (itemId == -1 || userId == -1 || userName.isNullOrEmpty()) {
            Toast.makeText(this, "Missing information", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        tvClaimantName.text = userName

        lifecycleScope.launch {
            val item = withContext(Dispatchers.IO) {
                AppDatabase.getInstance(this@ClaimActivity).itemDao().getItemById(itemId)
            }
            if (item == null) {
                Toast.makeText(this@ClaimActivity, "Item not found", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }
            tvClaimItem.text = "${item.name} (ID: ${item.id})"

            submitButton.setOnClickListener {
                val message = messageInput.text.toString().trim()
                if (message.isEmpty()) {
                    messageInput.error = "Enter your message"
                    return@setOnClickListener
                }
                if (item.ownerUserId == userId) {
                    Toast.makeText(this@ClaimActivity, "You cannot claim your own item", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // Prevent multiple submissions while saving
                submitButton.isEnabled = false
                lifecycleScope.launch {
                    try {
                        withContext(Dispatchers.IO) { val db = AppDatabase.getInstance(this@ClaimActivity)
                            // 1. Save the claim
                            val claim = Claim(itemId = itemId, claimantUserId = userId, claimantName = userName, message = message)
                            db.claimDao().insertClaim(claim)
                            // 2. Notify the owner of the item
                            val notification = NotificationEntity(
                                userId = item.ownerUserId,
                                title = "New Claim Request",
                                message = "$userName has requested your item: ${item.name}",
                                type = "CLAIM_REQUEST",
                                itemId = itemId
                            )
                            db.notificationDao().insertNotification(notification)
                        }
                        Toast.makeText(
                            this@ClaimActivity, "Claim submitted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } catch (e: Exception) {

                        submitButton.isEnabled = true

                        Toast.makeText(this@ClaimActivity, "Something went wrong: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
