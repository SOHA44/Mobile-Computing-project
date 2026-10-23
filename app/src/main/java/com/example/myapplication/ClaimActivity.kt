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
        val etClaimMessage = findViewById<EditText>(R.id.etClaimMessage)
        val btnSubmitClaim = findViewById<Button>(R.id.btnSubmitClaim)

        val itemId = intent.getIntExtra("ITEM_ID", -1)
        val userId = intent.getIntExtra("USER_ID", -1)
        val userName = intent.getStringExtra("USER_NAME")

        if (itemId == -1 || userId == -1 || userName.isNullOrEmpty()) {
            Toast.makeText(this, "Missing information", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvClaimItem.text = "Item ID: $itemId"
        tvClaimantName.text = userName

        btnSubmitClaim.setOnClickListener {
            val message = etClaimMessage.text.toString()

            if (message.isEmpty()) {
                etClaimMessage.error = "Enter your message"
            } else {
                val claim = Claim(
                    //itemId = itemId,
                    claimantUserId = userId,
                    claimantName = userName,
                    message = message,
                    status = "PENDING"
                )

//                lifecycleScope.launch(Dispatchers.IO) {
//                    user_database.getinstance(applicationContext)
//                        .ClaimDao()
//                        .insertClaim(claim)
//
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(
//                            this@ClaimActivity,
//                            "Claim submitted successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        finish()
//                    }
//                }
            }
        }
    }
}
