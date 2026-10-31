
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Signinpage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signinpage)

        val signinbtn = findViewById<Button>(R.id.btnSignIn)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        signinbtn.setOnClickListener {

            val userEmail = email.text.toString().trim().lowercase()
            val userPassword = password.text.toString()

            if (userEmail.isEmpty() || userPassword.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please fill all the fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            } else {
                lifecycleScope.launch(Dispatchers.IO) {

                    val user = AppDatabase.getInstance(this@Signinpage)
                        .userDao()
                        .signIn(userEmail, userPassword)
                    withContext(Dispatchers.Main) {
                        if (user != null) {
                            Toast.makeText(
                                this@Signinpage,
                                "Sign in successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            val homeIntent = Intent(
                                this@Signinpage,
                                HomeActivity::class.java
                            )
                            homeIntent.putExtra("userId", user.id)

                            startActivity(homeIntent)
                            finish()

                        } else {

                            Toast.makeText(
                                this@Signinpage,
                                "Invalid email or password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                }
            }
        }
    }
}

