package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signinpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signinpage)
        val signinbtn = findViewById<Button>(R.id.btnSignIn)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        signinbtn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {

                if (email.contains("@")) {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()}

            user_database.getinstance(this).userDao().signIn(email, password)
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }


    }
}}