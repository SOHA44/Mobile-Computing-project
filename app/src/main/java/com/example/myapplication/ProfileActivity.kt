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
        var name: String ="";
        var email: String ="";
        var phoneNumber: String = "";
        var faculty: String="";
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
    }


}