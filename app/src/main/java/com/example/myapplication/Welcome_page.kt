
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
            val intent = Intent(this, sign_page::class.java)
            startActivity(intent)
        }
    }
}

