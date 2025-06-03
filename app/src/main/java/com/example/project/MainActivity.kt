package com.example.project

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val buttonProfile = findViewById<ImageView>(R.id.buttonProfile)
        buttonProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_History -> Toast.makeText(this, "Menu Clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_bookmark -> Toast.makeText(this, "Cart Clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> {
                    Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val headerView = navigationView.getHeaderView(0)
        val profileImage = headerView.findViewById<ImageView>(R.id.profile_image)
        val userNameText = headerView.findViewById<TextView>(R.id.user_name)
        val userEmailText = headerView.findViewById<TextView>(R.id.user_email)

        val userName = intent.getStringExtra("userName")
        val userEmail = intent.getStringExtra("userEmail")
        val userType = intent.getStringExtra("userType")

        userNameText.text = userName
        userEmailText.text = userEmail

        Toast.makeText(this, "Welcome $userName ($userType)", Toast.LENGTH_LONG).show()

        loadWorkerProfiles()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun loadWorkerProfiles() {
        val profileContainer = findViewById<LinearLayout>(R.id.profileContainer)
        profileContainer.removeAllViews()

        val sharedPref = getSharedPreferences("reviews_prefs", MODE_PRIVATE)

        val workers = listOf(
            Worker("Alice", "123456", "Plumber", "5 years", "", 2),
            Worker("Bob", "987654", "Electrician", "3 years", "", 0),
            Worker("Charlie", "555666", "Painter", "4 years", "", 1),
            Worker("David", "888999", "Carpenter", "6 years", "", 3),
            Worker("Emma", "333222", "Welder", "2 years", "", 0)
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
                workerImage.setImageResource(R.drawable.ic_launcher_background)
            }

            val reportCount = sharedPref.getInt("report_${worker.name}", 0)
            if (reportCount >= 5) {
                workerCard.setBackgroundColor(Color.parseColor("#FFCDD2"))
            }

            workerCard.setOnClickListener {
                val intent = Intent(this, WorkerProfileActivity::class.java).apply {
                    putExtra("name", worker.name)
                    putExtra("contact", worker.contact)
                    putExtra("expertise", worker.expertise)
                    putExtra("experience", worker.experience)
                    putExtra("imageUri", worker.imageUri)
                    putExtra("reportCount", reportCount)
                }
                startActivity(intent)
            }

            profileContainer.addView(workerCard)
        }
    }
}