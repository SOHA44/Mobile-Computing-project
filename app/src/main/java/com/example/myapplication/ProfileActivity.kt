package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
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
//        val userId=intent.getIntExtra("userId",-1)
        val nameView=findViewById<TextView>(R.id.textView4)

        lifecycleScope.launch {
        val userdata= withContext(Dispatchers.IO){

            db.userDao().getUser(userId)
        }

            nameView.setText(userdata?.name)

        }
    }
}