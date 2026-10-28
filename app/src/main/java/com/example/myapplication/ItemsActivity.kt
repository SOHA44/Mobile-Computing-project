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
import android.app.DatePickerDialog
import android.widget.RadioButton
import android.widget.RadioGroup
import java.util.Calendar

class ItemsActivity : AppCompatActivity() {
    private var photoPath: String = ""
    private val cameraLauncher =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && photoPath.isNotEmpty()) {
                val bitmap = android.graphics.BitmapFactory.decodeFile(photoPath)
                findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
            } else {
                photoPath = ""
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_items)

       val userId = intent.getIntExtra("userId", -1)

        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

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
        val typeGroup = findViewById<RadioGroup>(R.id.item_type_group)
        val item_found=findViewById<RadioButton>(R.id.found_btn)
        val item_lost=findViewById<RadioButton>(R.id.lost_btn)
        var item_type="found"
        val item_db = AppDatabase.getInstance(this)
        val back=findViewById< ImageButton>(R.id.back_btn)
        date.setOnClickListener {
            val c = Calendar.getInstance()
            val dialog = DatePickerDialog(this, { _, year, month, day -> date.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = System.currentTimeMillis()
            dialog.show()
        }
        camera.setOnClickListener {
            val dir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
            val file = java.io.File.createTempFile("IMG_", ".jpg", dir)
            photoPath = file.absolutePath

            val uri = androidx.core.content.FileProvider.getUriForFile(
                this, "${applicationContext.packageName}.fileprovider", file
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            cameraLauncher.launch(cameraIntent)
        }

        save_button.setOnClickListener {
            val item_name = name.text.toString().trim()
            val item_description = description.text.toString().trim()
            val item_location = location.text.toString().trim()
            val item_date = date.text.toString().trim()
            val item_contact = contact.text.toString().trim()
            if(item_found.isChecked)
                item_type="found"
            else if(item_lost.isChecked)
                item_type="lost"
            if (item_name.isEmpty() || item_description.isEmpty() || item_location.isEmpty() || item_date.isEmpty() || item_contact.isEmpty()||typeGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener}
             else {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        item_db.itemDao().insertItem(
                            Items(
                                name = item_name,
                                description = item_description,
                                photo = photoPath,
                                date = item_date,
                                location = item_location,
                                contactInfo = item_contact,
                                ownerUserId = userId,
                                type = item_type))}
                        Toast.makeText(this@ItemsActivity, "Item saved successfully", Toast.LENGTH_SHORT).show()
                        name.text.clear()
                        description.text.clear()
                        location.text.clear()
                        date.text.clear()
                        contact.text.clear()
                        typeGroup.clearCheck()
                        photoPath = ""
                        image.setImageDrawable(null)

                }

            }
        }
        back.setOnClickListener { finish() }

    }}