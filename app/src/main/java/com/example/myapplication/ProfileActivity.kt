package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
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
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        val db= AppDatabase.getInstance(this)
        val sharedPreferences =
            getSharedPreferences("shared_per", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        var name: String ="";
        var email: String ="";
        var phoneNumber: String = "";
        var faculty: String="";
        val itemBtn = findViewById<Button>(R.id.btn_view_all_items)
        val lostItemBtn = findViewById<Button>(R.id.btn_view_lost)
        val foundItemBtn = findViewById<Button>(R.id.btn_view_found)
        val addClaimBtn = findViewById<Button>(R.id.btnAddClaim)
        val recievedClaimsBtn = findViewById<Button>(R.id.btnRecievedClaims)
        val notificationsBtn = findViewById<RelativeLayout>(R.id.notificationsProfile)
        val updateProfileBtn = findViewById<RelativeLayout>(R.id.updateProfile)
        val signOutBtn = findViewById<RelativeLayout>(R.id.signOutProfile)
        
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val user = db.userDao().getUser(userId)
                if (user != null) {
                    name = user.name
                    email = user.email
                    phoneNumber = user.phoneNumber
                    faculty = user.faculty
                }
            }
            findViewById<TextView>(R.id.tvProfName).text = name
            findViewById<TextView>(R.id.tvProfEmail).text = email
            findViewById<TextView>(R.id.tvProfFaculty).text = faculty
        }
        itemBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        lostItemBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("FILTER_TYPE", "MY_LOST")
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        foundItemBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("FILTER_TYPE", "MY_FOUND")
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        addClaimBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("FILTER_TYPE", "ALL_FOUND")
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        recievedClaimsBtn.setOnClickListener {
            val intent = Intent(this, ReceivedClaimsActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        notificationsBtn.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        updateProfileBtn.setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
        signOutBtn.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("isloggedin", false)
            editor.apply()
            val intent = Intent(this, sign_page::class.java)
            startActivity(intent)
            finish()
        }

    }
}

