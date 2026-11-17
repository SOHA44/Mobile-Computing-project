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
import android.widget.Toast
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
    private var currentuserId: Int = -1
    private var currentusetname: String = ""

    private var currentFilterType: String = "ALL"
    private lateinit var notificationBadge: TextView


    private fun view(list: List<Items>){

        val grid = findViewById<GridView>(R.id.gridView)

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
                    val photo = value as String

                    if (photo.isNotEmpty()) {
                        if (photo.startsWith("content://")) {
                            view.setImageURI(android.net.Uri.parse(photo))
                        } else {
                            val bitmap = BitmapFactory.decodeFile(photo)
                            view.setImageBitmap(bitmap)
                        }
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

        grid.setOnItemClickListener { _, _, position, _ ->
            val itemId = list[position].id
            val itemActivity =Intent(this@HomeActivity,ItemActivity::class.java)
            itemActivity.putExtra("itemId",itemId)
            itemActivity.putExtra("userId",currentuserId)
            itemActivity.putExtra("userName",currentusetname)
            startActivity(itemActivity)
        }


    }

    override fun onResume() {
        super.onResume()
        updateNotificationBadge()
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                val dao = AppDatabase.getInstance(this@HomeActivity).itemDao()
                when (currentFilterType) {
                    "MY_LOST" -> dao.getMyLostItems(currentuserId)
                    "MY_FOUND" -> dao.getMyFoundItems(currentuserId)
                    "ALL_FOUND" -> dao.getAllFoundItems()
                    "ALL_LOST" -> dao.getAllLostItems()
                    else -> dao.getAllItems()
                }
            }
            view(list)
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

        val profile =findViewById<View>(R.id.profile)
        val home =findViewById<View>(R.id.home)
        val log_out = findViewById<View>(R.id.logout)
        val notification =findViewById<View>(R.id.notification)
        notificationBadge = findViewById(R.id.notificationBadge)
        val receivedClaims =findViewById<View>(R.id.receivedClaims)
        val search =findViewById<EditText>(R.id.search)

        val all=findViewById<Button>(R.id.all)
        val found=findViewById<Button>(R.id.found)
        val lost=findViewById<Button>(R.id.lost)
        val adding_item=findViewById<Button>(R.id.add)

        currentuserId = intent.getIntExtra("userId", -1)
        currentusetname = intent.getStringExtra("userName") ?: ""
        currentFilterType = intent.getStringExtra("FILTER_TYPE") ?: "ALL"


        if (currentFilterType == "MY_FOUND" || currentFilterType == "MY_LOST") {
            all.visibility = View.GONE
            found.visibility = View.GONE
            lost.visibility = View.GONE
        }

        profile.setOnClickListener {
           val profileActivity= Intent(this, ProfileActivity::class.java)
            profileActivity.putExtra("userId",currentuserId)
           startActivity(profileActivity)
        }

        log_out.setOnClickListener {
            val shared = getSharedPreferences("shared_per", MODE_PRIVATE)
            val editor = shared.edit()
            editor.putBoolean("isloggedin", false)
            editor.apply()
            val intent = Intent(this, sign_page::class.java)
            startActivity(intent)
            finish()
        }

        notification.setOnClickListener {
            val notificationIntent = Intent(this@HomeActivity, NotificationActivity::class.java)
            notificationIntent.putExtra("userId", currentuserId)
            notificationIntent.putExtra("userName", currentusetname)
            startActivity(notificationIntent)
        }
        home.setOnClickListener {
            currentFilterType = "ALL"
            onResume()

            all.visibility = View.VISIBLE
            found.visibility = View.VISIBLE
            lost.visibility = View.VISIBLE
            all.backgroundTintList = ColorStateList.valueOf("#1A73E8".toColorInt())
            all.setTextColor(Color.WHITE)
            lost.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            lost.setTextColor(Color.BLACK)
            found.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            found.setTextColor(Color.BLACK)
        }

        receivedClaims.setOnClickListener {
            val claimsIntent = Intent(this@HomeActivity, ReceivedClaimsActivity::class.java)
            claimsIntent.putExtra("userId", currentuserId)
            startActivity(claimsIntent)
        }



        adding_item.setOnClickListener {
            val addItem= Intent(this, ItemsActivity::class.java)
            addItem.putExtra("userId", currentuserId)
            startActivity(addItem)
        }

        found.setOnClickListener {
            currentFilterType = "ALL_FOUND"
            onResume()
            found.backgroundTintList = ColorStateList.valueOf("#1A73E8".toColorInt())
            found.setTextColor(Color.WHITE)
            all.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            all.setTextColor(Color.BLACK)
            lost.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            lost.setTextColor(Color.BLACK)


        }

        lost.setOnClickListener {
            currentFilterType = "ALL_LOST"
            onResume()
            lost.backgroundTintList = ColorStateList.valueOf("#1A73E8".toColorInt())
            lost.setTextColor(Color.WHITE)
            all.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            all.setTextColor(Color.BLACK)
            found.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            found.setTextColor(Color.BLACK)
        }

        all.setOnClickListener {
            currentFilterType = "ALL"
            onResume()
            all.backgroundTintList = ColorStateList.valueOf("#1A73E8".toColorInt())
            all.setTextColor(Color.WHITE)
            lost.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            lost.setTextColor(Color.BLACK)
            found.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            found.setTextColor(Color.BLACK)
        }


    }
    private fun updateNotificationBadge() {
        lifecycleScope.launch {
            val unreadCount = withContext(Dispatchers.IO) {
                AppDatabase.getInstance(this@HomeActivity).notificationDao().getUnreadCount(currentuserId)
            }

            if (unreadCount > 0) {
                notificationBadge.text = if (unreadCount > 99) "99+" else unreadCount.toString()
                notificationBadge.visibility = View.VISIBLE
            } else {
                notificationBadge.visibility = View.GONE
            }
        }
    }
}