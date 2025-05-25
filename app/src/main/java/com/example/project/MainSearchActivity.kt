package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainSearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_search_activity)

        val menuIcon = findViewById<ImageView>(R.id.menuIcon)

        menuIcon.setOnClickListener {
            // Use a callback or directly tell MainActivity to open the drawer
            // For now, you can assume this activity is embedded as a fragment or inflated inside MainActivity
            // If you're embedding this layout, you should move this logic to MainActivity
        }
    }
}
