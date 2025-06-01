package com.example.project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainSearchActivity : AppCompatActivity() {

    private lateinit var userDatabaseHelper: UserDatabaseHelper
    private lateinit var profileContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_search_activity)

        profileContainer = findViewById(R.id.profileContainer)
        userDatabaseHelper = UserDatabaseHelper(this)

        loadWorkerProfiles()
    }

    private fun loadWorkerProfiles() {
        // Clear any previous views
        profileContainer.removeAllViews()

        // Fetch workers
        val workers = userDatabaseHelper.getAllWorkers()
        println("Workers loaded: ${workers.size}")  // Debug log

        for (worker in workers) {
            val profileView = LayoutInflater.from(this).inflate(R.layout.worker_card, profileContainer, false)

            val nameTextView = profileView.findViewById<TextView>(R.id.workerName)
            val contactTextView = profileView.findViewById<TextView>(R.id.workerContact)
            val expertiseTextView = profileView.findViewById<TextView>(R.id.workerExpertise)
            val experienceTextView = profileView.findViewById<TextView>(R.id.workerExperience)
            val imageView = profileView.findViewById<ImageView>(R.id.workerImage)

            nameTextView.text = "Name: ${worker.name}"
            contactTextView.text = "Contact: ${worker.contact}"
            expertiseTextView.text = "Expertise: ${worker.expertise}"
            experienceTextView.text = "Experience: ${worker.experience}"

            if (worker.imageUri.isNotEmpty()) {
                imageView.setImageURI(Uri.parse(worker.imageUri))
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }

            // Profile click listener
            profileView.setOnClickListener {
                val intent = Intent(this, WorkerProfileActivity::class.java)
                intent.putExtra("worker", worker)  // Pass the User object
                startActivity(intent)
            }

            profileContainer.addView(profileView)
        }
    }
}
