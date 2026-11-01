
package com.example.myapplication

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class Welcome_page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.welcome_activity)

        val btn = findViewById<Button>(R.id.btngetstarted)

        btn.setOnClickListener {
            val shared_per = getSharedPreferences("shared_per", MODE_PRIVATE)
            val isloggedin = shared_per.getBoolean("isloggedin",false)
            if(isloggedin){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
              else  {
            val intent = Intent(this, sign_page::class.java)
            startActivity(intent)
        }
            finish()
        }
    }
}

