package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class UpdateProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backBtn = findViewById<ImageButton>(R.id.btnBackProfile)
        val submitButton = findViewById<Button>(R.id.btnSubmit)

        var db = AppDatabase.getInstance(this)

        val sharedPreferences =
            getSharedPreferences("shared_per", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)


        backBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        submitButton.setOnClickListener {
            lifecycleScope.launch {
                val user = db.userDao().getUser(userId)
                val password = user?.password
                val newName = findViewById<EditText>(R.id.etUpdateName).text.toString().trim()
                val oldPassword = findViewById<EditText>(R.id.etCurrentPassword).text.toString().trim()
                val newPhoneNumber = findViewById<EditText>(R.id.editTextPhone).text.toString().trim()
                val newPassword = findViewById<EditText>(R.id.etNewPassword).text.toString().trim()
                val confirmPassword = findViewById<EditText>(R.id.etConfirmPassword).text.toString().trim()

                if (!oldPassword.equals(password)&&oldPassword.isNotEmpty())
                {
                    Toast.makeText(
                        this@UpdateProfileActivity,
                        "CURRENT PASSWORD INCORRECT",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch

                }else if(oldPassword.isNotEmpty())
                {
                    if (newPassword.isEmpty()) {
                        Toast.makeText(
                            this@UpdateProfileActivity,
                            "Please,write new password",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch

                    }else
                    {
                        if (newPassword.equals(confirmPassword)) {
                            db.userDao().updatePassword(userId, newPassword)
                        }
                        else
                        {
                            Toast.makeText(
                                this@UpdateProfileActivity,
                                "CONFIRM PASSWORD NOT MATCH",
                                Toast.LENGTH_LONG
                            ).show()
                            return@launch
                        }

                    }

                }

                if (newName.isNotEmpty()) {
                    db.userDao().updateName(userId, newName)
                }

                if (newPhoneNumber.isNotEmpty()) {
                    db.userDao().updatePhoneNumber(userId, newPhoneNumber)
                }


                Toast.makeText(
                    this@UpdateProfileActivity,
                    "Profile updated successfully",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@UpdateProfileActivity, ProfileActivity::class.java)
                startActivity(intent)

                finish()

            }
        }
    }
}