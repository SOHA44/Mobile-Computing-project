
package com.example.myapplication

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Welcome_page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.welcome_activity)

        val btn = findViewById<Button>(R.id.btngetstarted)

        btn.setOnClickListener {
            val shared_per = getSharedPreferences("shared_per", MODE_PRIVATE)
            val isloggedin = shared_per.getBoolean("isloggedin", false)
            if (isloggedin) {
                val userId = shared_per.getInt("userId", -1)
                val userName = shared_per.getString("userName", "") ?: ""
                if (userId != -1) {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("userId", userId)
                    intent.putExtra("userName", userName)
                    startActivity(intent)
                } else {
                    // Invalid saved session
                    shared_per.edit().putBoolean("isloggedin", false).apply()
                    val intent = Intent(this, SignActivity::class.java)
                    startActivity(intent)
                }
            } else {
                val intent = Intent(this, SignActivity::class.java)
                startActivity(intent)
            }

            finish()
        }
    }
}
