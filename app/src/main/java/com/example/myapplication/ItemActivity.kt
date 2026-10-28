package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
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




        val itemId = intent.getIntExtra("itemId", -1)


        lifecycleScope.launch {
            val itemData = withContext(Dispatchers.IO) {
                db.itemDao().getItemById(itemId)
            }
            item_name.setText(itemData?.name)


        }
    }
}