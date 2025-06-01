package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

import android.net.Uri
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        navigationView = findViewById<NavigationView>(R.id.navigationView)

        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val buttonProfile = findViewById<ImageView>(R.id.buttonProfile)
        buttonProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Set NavigationItemSelectedListener
            navigationView.setNavigationItemSelectedListener { menuItem ->
                val itemId = menuItem.itemId

                if (itemId == R.id.nav_History) {
                    Toast.makeText(this@MainActivity, "Menu Clicked", Toast.LENGTH_SHORT).show()
                }
                if (itemId == R.id.nav_bookmark) {
                    Toast.makeText(this@MainActivity, "Cart Clicked", Toast.LENGTH_SHORT).show()
                }
                if (itemId == R.id.nav_logout) {
                    Toast.makeText(
                        this@MainActivity,
                        "You have been logged out",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }

            val headerView = navigationView.getHeaderView(0)
            val profileImage = headerView.findViewById<ImageView>(R.id.profile_image)
            val userNameText = headerView.findViewById<TextView>(R.id.user_name)
            val userEmailText = headerView.findViewById<TextView>(R.id.user_email)

            // profileImage.setOnClickListener {
            //    startActivity(Intent(this, ProfileActivity::class.java))
            // }

            // userNameText.setOnClickListener {
            //    startActivity(Intent(this, ProfileActivity::class.java))
            // }

            // Retrieve user info from intent
            val userName = intent.getStringExtra("userName")
            val userEmail = intent.getStringExtra("userEmail")
            val userType = intent.getStringExtra("userType")

            // Update UI
            userNameText.text = userName
            userEmailText.text = userEmail

            Toast.makeText(this, "Welcome $userName ($userType)", Toast.LENGTH_LONG).show()
            loadWorkerProfiles()
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return if (toggle.onOptionsItemSelected(item)) true
            else super.onOptionsItemSelected(item)
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

            val userDatabaseHelper = UserDatabaseHelper(this)
            val workers = userDatabaseHelper.getAllWorkers()
            println("Workers count: ${workers.size}")
            for (worker in workers) {
                val workerCard =
                    layoutInflater.inflate(R.layout.worker_card, profileContainer, false)

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

                profileContainer.addView(workerCard)
            }
        }
    }