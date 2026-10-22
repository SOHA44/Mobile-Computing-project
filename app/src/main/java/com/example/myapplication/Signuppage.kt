package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signuppage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signuppage)
        val spinnerFaculty = findViewById<Spinner>(R.id.spinnerFaculty)
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

    }
}