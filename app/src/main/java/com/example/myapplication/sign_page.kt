package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent

class sign_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_activity)
        val signinbtn = findViewById<Button>(R.id.btnSignIn)
        signinbtn.setOnClickListener {
            val intent = Intent(this, Signinpage::class.java)
            startActivity(intent)
        }
        val signupbtn = findViewById<Button>(R.id.btnSignUp)
        signupbtn.setOnClickListener {
            val intent = Intent(this, Signuppage::class.java)
            startActivity(intent)
        }
    }
}

