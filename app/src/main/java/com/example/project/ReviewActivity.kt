package com.example.project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReviewActivity : AppCompatActivity() {


    private lateinit var editTextReview: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        editTextReview = findViewById(R.id.editTextReview)
        submitButton = findViewById(R.id.buttonSubmit)

        submitButton.setOnClickListener {
            val reviewText = editTextReview.text.toString().trim()
            if (reviewText.isEmpty()) {
                Toast.makeText(this, "Please enter your review.", Toast.LENGTH_SHORT).show()
            } else {
                // Retrieve the user's name from SharedPreferences
                val sharedPref = getSharedPreferences("reviews_prefs", MODE_PRIVATE)
                val reviewerName = sharedPref.getString("logged_in_username", "Anonymous") ?: "Anonymous"

                // Send both review text and reviewer name back
                val resultIntent = Intent()
                resultIntent.putExtra("reviewText", reviewText)
                resultIntent.putExtra("reviewerName", reviewerName)
                setResult(Activity.RESULT_OK, resultIntent)

                Toast.makeText(this, "Thank you for your review!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}