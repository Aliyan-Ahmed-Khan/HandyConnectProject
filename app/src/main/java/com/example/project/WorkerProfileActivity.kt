package com.example.project

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class WorkerProfileActivity : AppCompatActivity() {

    private lateinit var reviewsContainer: LinearLayout
    private val reviewsList = mutableListOf<String>()

    private lateinit var name: String
    private lateinit var sharedPref: SharedPreferences
    private lateinit var workerReportCount: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_profile)

        name = intent.getStringExtra("name") ?: ""
        val contact = intent.getStringExtra("contact")
        val expertise = intent.getStringExtra("expertise")
        val experience = intent.getStringExtra("experience")
        val imageUri = intent.getStringExtra("imageUri")

        sharedPref = getSharedPreferences("reviews_prefs", MODE_PRIVATE)

        val workerImage = findViewById<ImageView>(R.id.workerProfileImage)
        val workerName = findViewById<TextView>(R.id.workerProfileName)
        val workerContact = findViewById<TextView>(R.id.workerProfileContact)
        val workerExpertise = findViewById<TextView>(R.id.workerProfileExpertise)
        val workerExperience = findViewById<TextView>(R.id.workerProfileExperience)
        workerReportCount = findViewById(R.id.workerReportCount)
        val reviewButton = findViewById<Button>(R.id.buttonReview)
        val callButton = findViewById<Button>(R.id.buttonCall)
        reviewsContainer = findViewById(R.id.reviewsContainer)

        val resetReportsButton = findViewById<ImageButton>(R.id.reportButton)
        resetReportsButton.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            intent.putExtra("name", name)
            startActivityForResult(intent,1002)
        }

        workerName.text = "Name: $name"
        workerContact.text = "Contact: $contact"
        workerExpertise.text = "Expertise: $expertise"
        workerExperience.text = "Experience: $experience"

        workerName.setTextColor(Color.BLACK)
        workerContact.setTextColor(Color.BLACK)
        workerExpertise.setTextColor(Color.BLACK)
        workerExperience.setTextColor(Color.BLACK)

        if (!imageUri.isNullOrEmpty()) {
            workerImage.setImageURI(Uri.parse(imageUri))
        } else {
            workerImage.setImageResource(R.drawable.person)
        }

        updateReportCountDisplay()

        reviewButton.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("workerName", name)
            startActivityForResult(intent, 1001)
        }

        callButton.setOnClickListener {
            if (!contact.isNullOrEmpty()) {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:$contact")
                startActivity(dialIntent)
            } else {
                Toast.makeText(this, "Contact number not available", Toast.LENGTH_SHORT).show()
            }
        }

        loadReviews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.worker_profile_menu, menu)
        return true
    }

    private fun updateReportCountDisplay() {
        val reportCount = sharedPref.getInt("report_$name", 0)
        workerReportCount.text = "Reports: $reportCount"

        if (reportCount >= 5) {
            workerReportCount.setTextColor(Color.RED)
            Toast.makeText(
                this,
                "$name's profile has had been reported multiple times now. We suggest you to look for someone else.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            workerReportCount.setTextColor(Color.BLACK)
        }
        Toast.makeText(this, "Report count refreshed", Toast.LENGTH_SHORT).show()
    }

    private fun saveReviews() {
        val editor = sharedPref.edit()
        val reviewsStr = reviewsList.joinToString("||")
        editor.putString("reviews_$name", reviewsStr)
        editor.apply()
    }

    private fun loadReviews() {
        reviewsContainer.removeAllViews()
        val reviewsStr = sharedPref.getString("reviews_$name", "")
        reviewsList.clear()
        if (!reviewsStr.isNullOrEmpty()) {
            reviewsList.addAll(reviewsStr.split("||"))
        }

        if (reviewsList.isEmpty()) {
            val noReviews = TextView(this)
            noReviews.text = "No reviews yet."
            noReviews.setPadding(8, 8, 8, 8)
            noReviews.setTextColor(Color.GRAY)
            reviewsContainer.addView(noReviews)
        } else {
            for (review in reviewsList) {
                val reviewText = TextView(this)
                reviewText.text = review
                reviewText.setPadding(16, 8, 16, 8)
                reviewText.setBackgroundResource(R.drawable.review_background)
                reviewText.setTextColor(Color.BLACK)
                reviewsContainer.addView(reviewText)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // REVIEW
            val reviewText = data?.getStringExtra("reviewText") ?: return
            val reviewerName = data.getStringExtra("reviewerName") ?: "Anonymous"

            val fullReview = "$reviewText\n- by $reviewerName"
            reviewsList.add(fullReview)
            saveReviews()
            loadReviews()

        } else if (requestCode == 1002 && resultCode == RESULT_OK) {
            // REPORT
            updateReportCountDisplay()
        }
    }
}