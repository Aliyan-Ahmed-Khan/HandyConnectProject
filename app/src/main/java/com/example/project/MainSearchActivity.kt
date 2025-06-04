package com.example.project

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainSearchActivity : AppCompatActivity() {


    private lateinit var profileContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileContainer = findViewById(R.id.profileContainer)
        loadWorkerProfiles()
    }

    private fun loadWorkerProfiles() {
        val sharedPref = getSharedPreferences("reviews_prefs", MODE_PRIVATE)
        val loggedInUserName = sharedPref.getString("logged_in_username", "Anonymous") ?: "Anonymous"

        profileContainer.removeAllViews()

        val workers = listOf(
            Worker("Fahad", "123456", "Plumber", "5 years", "", 2),
            Worker("Ali", "987654", "Electrician", "3 years", "", 4),
            Worker("Charlie", "555666", "Painter", "4 years", "", 5),
            Worker("David", "888999", "Carpenter", "6 years", "", 0),
            Worker("Emma", "333222", "Welder", "2 years", "", 1)
        )

        for (worker in workers) {
            val workerCard = layoutInflater.inflate(R.layout.worker_card, profileContainer, false)

            val workerImage = workerCard.findViewById<ImageView>(R.id.workerImage)
            val workerName = workerCard.findViewById<TextView>(R.id.workerName)
            val workerContact = workerCard.findViewById<TextView>(R.id.workerContact)
            val workerExpertise = workerCard.findViewById<TextView>(R.id.workerExpertise)
            val workerExperience = workerCard.findViewById<TextView>(R.id.workerExperience)

            workerName.text = "Name: ${worker.name}"
            workerContact.text = "Contact: ${worker.contact}"
            workerExpertise.text = "Expertise: ${worker.expertise}"
            workerExperience.text = "Experience: ${worker.experience}"

            if (worker.imageUri.isNotEmpty()) {
                workerImage.setImageURI(Uri.parse(worker.imageUri))
            } else {
                workerImage.setImageResource(R.drawable.person)
            }

            val reportCount = sharedPref.getInt("report_${worker.name}", 0)
            if (reportCount >= 5) {
                workerCard.setBackgroundColor(Color.parseColor("#FFCDD2")) // Light red
            }

            workerCard.setOnClickListener {
                val intent = Intent(this, WorkerProfileActivity::class.java).apply {
                    putExtra("name", worker.name)
                    putExtra("contact", worker.contact)
                    putExtra("expertise", worker.expertise)
                    putExtra("experience", worker.experience)
                    putExtra("imageUri", worker.imageUri)
                    putExtra("reportCount", reportCount)
                    putExtra("userName", loggedInUserName) // pass username to next activity
                }
                startActivity(intent)
            }

            profileContainer.addView(workerCard)
        }
    }
}