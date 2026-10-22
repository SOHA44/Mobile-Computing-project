package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.User
import com.example.myapplication.user_database
import kotlinx.coroutines.launch
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signuppage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signuppage)
        val spinnerFaculty = findViewById<Spinner>(R.id.spinnerFaculty)
        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val phone = findViewById<EditText>(R.id.phone)
        val signup = findViewById<Button>(R.id.btnSignUp)
        val faculties = arrayOf(
            "Choose Your Faculty",
            "Faculty of Computer and Information Sciences",
            "Faculty of Engineering",
            "Faculty of Medicine",
            "Faculty of Dentistry",
            "Faculty of Pharmacy",
            "Faculty of Science",
            "Faculty of Business",
            "Faculty of Al-Alsun",
            "Faculty of Arts",
            "Faculty of Education",
            "Faculty of Law",
        )
        spinnerFaculty.setSelection(0)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            faculties
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerFaculty.adapter = adapter
signup.setOnClickListener {
    val name = name.text.toString()
    val email = email.text.toString()
    val password = password.text.toString()
    val phone = phone.text.toString()
    val faculty = spinnerFaculty.selectedItem.toString()
    if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || faculty == "Choose Your Faculty") {
        Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
    } else {
        if (email.contains("@")) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }
        if (password != confirmPassword.text.toString()) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }
        if (phone.length != 11) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }
        lifecycleScope.launch {
                val user = User(name =name, email = email, password = password, phoneNumber =  phone, faculty = faculty)
                user_database.getinstance(this@Signuppage)
                    .userDao()
                    .signUp(user)

                Toast.makeText(
                    this@Signuppage,
                    "Sign up successful",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent(this@Signuppage, Signinpage::class.java))
                finish()

            }
                Toast.makeText(
                    this@Signuppage,
                    "Sign up failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
       intent = Intent(this, HomeActivity::class.java)
    }
}
