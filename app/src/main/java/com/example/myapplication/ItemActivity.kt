package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast


class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item)
        val db= AppDatabase.getInstance(this)
        val item_name = findViewById<TextView>(R.id.tv_itemname)
        val item_description = findViewById<TextView>(R.id.tv_description)
        val item_location = findViewById<TextView>(R.id.tv_location)
        val item_date = findViewById<TextView>(R.id.tv_date)
        val item_contact = findViewById<TextView>(R.id.tv_contact)
        val item_type = findViewById<TextView>(R.id.tv_itemtype)
        val item_image = findViewById<ImageView>(R.id.iv_itemimage)
        val back=findViewById<ImageButton>(R.id.back_btn)

        val claimButton = findViewById<Button>(R.id.btnClaim)


        val itemId = intent.getIntExtra("itemId", -1)
        val userId = intent.getIntExtra("userId", -1)
        val userName = intent.getStringExtra("userName")



        lifecycleScope.launch {
            val itemData = withContext(Dispatchers.IO) {
                db.itemDao().getItemById(itemId)
            }
            if (itemData == null) {
                Toast.makeText(this@ItemActivity, "Item not found", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }
            item_name.text = itemData.name
            item_description.text = itemData.description
            item_location.text = "Location: ${itemData.location}"
            item_date.text = "Date: ${itemData.date}"
            item_contact.text = "Contact: ${itemData.contactInfo}"
            item_type.text = if (itemData.type == "lost") {
                "Lost Item"
            } else {
                "Found Item"
            }
            val photo = itemData.photo
            if (photo.isNotEmpty()) {
                if (photo.startsWith("content://")) {
                    item_image.setImageURI(android.net.Uri.parse(photo))
                } else {
                    val bitmap = BitmapFactory.decodeFile(photo)
                    item_image.setImageBitmap(bitmap)
                }
            }

            claimButton.setOnClickListener {
                val claimIntent = Intent(this@ItemActivity, ClaimActivity::class.java)
                claimIntent.putExtra("ITEM_ID", itemId)
                claimIntent.putExtra("USER_ID", userId)
                claimIntent.putExtra("USER_NAME", userName)
                startActivity(claimIntent)
            }
            back.setOnClickListener { finish() }


        }
    }
}