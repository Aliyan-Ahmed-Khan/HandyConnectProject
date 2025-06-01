package com.example.project

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class WorkerProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_profile)

        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val profileName = findViewById<TextView>(R.id.profileName)
        val profileContact = findViewById<TextView>(R.id.profileContact)
        val profileEmail = findViewById<TextView>(R.id.profileEmail)
        val profileExpertise = findViewById<TextView>(R.id.profileExpertise)
        val profileExperience = findViewById<TextView>(R.id.profileExperience)
        val profileLocation = findViewById<TextView>(R.id.profileLocation)
        val profileGender = findViewById<TextView>(R.id.profileGender)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val tvRating = findViewById<TextView>(R.id.tvRating)

        val btnReview = findViewById<Button>(R.id.btnReview)
        val btnReport = findViewById<Button>(R.id.btnReport)

        val worker = intent.getSerializableExtra("worker") as? User

        worker?.let { user ->
            profileName.text = user.name
            profileContact.text = "Contact: ${user.contact}"
            profileEmail.text = "Email: ${user.email}"
            profileExpertise.text = "Expertise: ${user.expertise}"
            profileExperience.text = "Experience: ${user.experience}"
            profileLocation.text = "Location: ${user.location}"
            profileGender.text = "Gender: ${user.gender}"

            if (user.imageUri.isNotEmpty()) {
                profileImage.setImageURI(Uri.parse(user.imageUri))
            } else {
                profileImage.setImageResource(R.drawable.ic_launcher_background)
            }

            // Update rating text when user changes rating
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                tvRating.text = String.format("%.1f", rating)
                Toast.makeText(this, "You rated ${user.name} $rating stars", Toast.LENGTH_SHORT).show()
                // Save to DB if needed
            }

            btnReview.setOnClickListener {
                Toast.makeText(this, "Open Review page for ${user.name}", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, ReviewActivity::class.java))
            }

            btnReport.setOnClickListener {
                Toast.makeText(this, "Open Report page for ${user.name}", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this, ReportActivity::class.java))
            }
        } ?: run {
            Toast.makeText(this, "Worker data not found!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
