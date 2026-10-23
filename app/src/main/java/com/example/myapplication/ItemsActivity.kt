package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemsActivity : AppCompatActivity() {
    private var photoPath: String = ""
    private val cameraLauncher =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts
                .StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? android.graphics.Bitmap
                if (bitmap != null) {
                    findViewById<ImageView>(R.id.imageView)
                        .setImageBitmap(bitmap)
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val name = findViewById<EditText>(R.id.etname)
        val description = findViewById<EditText>(R.id.etdescription)
        val location = findViewById<EditText>(R.id.et_location)
        val date = findViewById<EditText>(R.id.et_date)
        val contact = findViewById<EditText>(R.id.et_number)
        val image = findViewById<ImageView>(R.id.imageView)
        val camera = findViewById<ImageButton>(R.id.imageButton)
        val save_button = findViewById<Button>(R.id.save_btn)
        val item_db = Itemdatabase.getInstance(this)
        save_button.setOnClickListener {
            val item_name = name.text.toString().trim()
            val item_description = description.text.toString().trim()
            val item_location = location.text.toString().trim()
            val item_date = date.text.toString().trim()
            val item_contact = contact.text.toString().trim()
            if (
                item_name.isEmpty() ||
                item_description.isEmpty() ||
                item_location.isEmpty() ||
                item_date.isEmpty() ||
                item_contact.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                lifecycleScope.launch {

                    withContext(Dispatchers.IO) {
                        item_db.itemDao().insertItem(
                            Items(
                                name = item_name,
                                description = item_description,
                                photo = photoPath,
                                date = item_date,
                                location = item_location,
                                contactInfo = item_contact
                            )
                        )
                        Toast.makeText(
                            this@ItemsActivity,
                            "Item saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        name.text.clear()
                        description.text.clear()
                        location.text.clear()
                        date.text.clear()
                        contact.text.clear()
                        photoPath = ""
                        image.setImageDrawable(null)
                    }
                }
                camera.setOnClickListener {
                    intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivity(intent)
                }
            }
        }

    }}