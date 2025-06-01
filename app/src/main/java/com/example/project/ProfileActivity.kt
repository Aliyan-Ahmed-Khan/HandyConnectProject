package com.example.project

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.project.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val editIcon = findViewById<ImageView>(R.id.edit_icon)
        val nameField = findViewById<EditText>(R.id.edit_name)
        val emailField = findViewById<EditText>(R.id.edit_email)
        val ageField = findViewById<EditText>(R.id.edit_age)
        val descField = findViewById<EditText>(R.id.edit_description)

        editIcon.setOnClickListener {
            nameField.isEnabled = true
            ageField.isEnabled = true
            descField.isEnabled = true
        }
    }
}
