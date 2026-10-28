package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.graphics.toColorInt

class HomeActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        val grid = findViewById<GridView>(R.id.gridView)
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                AppDatabase.getInstance(this@HomeActivity).itemDao().getAllItems()
            }

            val data = list.map {
                mapOf("photo" to it.photo, "name" to it.name, "location" to it.location,"type" to it.type)
            }

            val adapter = SimpleAdapter(
                this@HomeActivity,
                data,
                R.layout.item_grid,
                arrayOf("photo", "name", "location","type"),
                intArrayOf(R.id.itemImage, R.id.itemName, R.id.itemLocation,R.id.itemType)
            )

            adapter.viewBinder = SimpleAdapter.ViewBinder { view, value, _ ->
                when {
                    view is ImageView -> {
                        val path = value as String
                        if (path.isNotEmpty()) {
                            view.setImageBitmap(BitmapFactory.decodeFile(path))
                        } else {
                            view.setImageDrawable(null)
                        }
                        true
                    }
                    view.id == R.id.itemType -> {
                        val type = view as TextView
                        if (value == "lost") {
                            type.text = "Lost"
                            type.setTextColor("#8A3B12".toColorInt())
                            type.backgroundTintList = ColorStateList.valueOf("#FDE6D3".toColorInt())
                        } else {
                            type.text = "Found"
                            type.setTextColor("#1E5A3C".toColorInt())
                            type.backgroundTintList = ColorStateList.valueOf("#D8EFE3".toColorInt())
                        }
                        true
                    }
                    else -> false
                }
            }
            grid.adapter = adapter

            //grid.onItemSelectedListener
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getIntExtra("userId", -1)

        val profile =findViewById<View>(R.id.profile)
        val notification =findViewById<View>(R.id.notification)
        val search =findViewById<EditText>(R.id.search)

        val all=findViewById<Button>(R.id.all)
        val found=findViewById<Button>(R.id.found)
        val lost=findViewById<Button>(R.id.lost)
        val adding_item=findViewById<Button>(R.id.add)
        var db= AppDatabase.getInstance(this)
        //var all_items=db.itemDao().getAllItems()
       // val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,all_items)


        profile.setOnClickListener {
//            intent= Intent(this, ProfileActivity::class.java)
//            startActivity(intent)
        }

        notification.setOnClickListener {
//            intent= Intent(this, NotificationActivity::class.java)
//            startActivity(intent)
        }

        adding_item.setOnClickListener {
            val addItem= Intent(this, ItemsActivity::class.java)
            addItem.putExtra("userId", userId)
            startActivity(addItem)
        }

    }
}