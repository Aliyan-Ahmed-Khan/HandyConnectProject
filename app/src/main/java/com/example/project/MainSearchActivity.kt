package com.example.project

import android.content.Intent

import android.os.Bundle

import android.widget.ImageButton

import android.widget.LinearLayout

import androidx.appcompat.app.AppCompatActivity

class MainSearchActivity : AppCompatActivity() {

    private lateinit var profileContainer: LinearLayout
    private lateinit var userDatabaseHelper: UserDatabaseHelper
    private lateinit var profileButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_search_activity)

//        profileContainer = findViewById(R.id.profileContainer)
        profileButton = findViewById(R.id.buttonProfile)
        userDatabaseHelper = UserDatabaseHelper(this)

//        loadWorkerProfiles()

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

//    private fun loadWorkerProfiles() {
//        profileContainer.removeAllViews()
//
//        val workers = userDatabaseHelper.getAllWorkers()
//        println("Workers count: ${workers.size}")
//        for (worker in workers) {
//            val workerCard = LayoutInflater.from(this).inflate(R.layout.worker_card, profileContainer, true)
//
//            val workerImage = workerCard.findViewById<ImageView>(R.id.workerImage)
//            val workerName = workerCard.findViewById<TextView>(R.id.workerName)
//            val workerContact = workerCard.findViewById<TextView>(R.id.workerContact)
//            val workerExpertise = workerCard.findViewById<TextView>(R.id.workerExpertise)
//            val workerExperience = workerCard.findViewById<TextView>(R.id.workerExperience)
//
//            workerName.text = "Name: ${worker.name}"
//            workerContact.text = "Contact: ${worker.contact}"
//            workerExpertise.text = "Expertise: ${worker.expertise}"
//            workerExperience.text = "Experience: ${worker.experience}"
//
//            if (worker.imageUri.isNotEmpty()) {
//                workerImage.setImageURI(Uri.parse(worker.imageUri))
//            } else {
//                workerImage.setImageResource(R.drawable.ic_launcher_background)
//            }
//            profileContainer.addView(workerCard)
//        }
//    }
}
