package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
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

class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item)
        val db= AppDatabase.getInstance(this)
        val item_name=findViewById<TextView>(R.id.textView2)

        val claimButton = findViewById<Button>(R.id.btnClaim)


        val itemId = intent.getIntExtra("itemId", -1)
        val userId = intent.getIntExtra("userId", -1)
        val userName = intent.getStringExtra("userName")



        lifecycleScope.launch {
            val itemData = withContext(Dispatchers.IO) {
                db.itemDao().getItemById(itemId)
            }
            item_name.setText(itemData?.name)

            claimButton.setOnClickListener {
                val claimIntent = Intent(this@ItemActivity, ClaimActivity::class.java)
                claimIntent.putExtra("ITEM_ID", itemId)
                claimIntent.putExtra("USER_ID", userId)
                claimIntent.putExtra("USER_NAME", userName)
                startActivity(claimIntent)
            }


        }
    }
}