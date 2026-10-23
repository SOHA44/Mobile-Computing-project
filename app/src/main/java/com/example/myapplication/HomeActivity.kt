package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {


    override fun onResume() {
        super.onResume()
        val items = findViewById<GridView>(R.id.gridView)
        lifecycleScope.launch {
            val names = withContext(Dispatchers.IO) {
                AppDatabase.getInstance(this@HomeActivity)
                    .itemDao()
                    .getAllItems()
                    .map { it.name }
            }
            items.adapter = ArrayAdapter(
                this@HomeActivity,
                android.R.layout.simple_list_item_1,
                names
            )
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