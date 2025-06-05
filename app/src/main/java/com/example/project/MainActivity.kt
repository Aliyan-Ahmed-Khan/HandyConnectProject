package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var searchDropdown: AutoCompleteTextView
    private lateinit var profileContainer: LinearLayout
    private lateinit var noResultsText: TextView

    private val allWorkers = listOf(
        Worker("Aliyan", "03171224600", "Plumber", "5 years", "", 0),
        Worker("Ibad", "03473291584", "Electrician", "3 years", "", 0),
        Worker("Obaid", "03483486238", "Painter", "4 years", "", 0),
        Worker("Minhaj", "03102030405", "Carpenter", "6 years", "", 0),
        Worker("Fahad", "03171234567", "Welder", "2 years", "", 0),
        Worker("Jawwwad", "03171237643", "Mechanic", "2 years", "", 0),
        Worker("Ahsan", "03829234567", "Carpenter", "3 years", "", 0),
        Worker("Salman", "03171234123", "Electrician", "6 years", "", 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        searchDropdown = findViewById(R.id.searchDropdown)
        profileContainer = findViewById(R.id.profileContainer)
        noResultsText = findViewById(R.id.noResultsText) // Make sure you add this in your XML

        findViewById<ImageView>(R.id.menuIcon).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        findViewById<ImageView>(R.id.buttonProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        val headerView = navigationView.getHeaderView(0)
        val userName = intent.getStringExtra("userName")
        val userEmail = intent.getStringExtra("userEmail")
        val userType = intent.getStringExtra("userType")

        headerView.findViewById<TextView>(R.id.user_name).text = userName
        headerView.findViewById<TextView>(R.id.user_email).text = userEmail

        Toast.makeText(this, "Welcome $userName ($userType)", Toast.LENGTH_SHORT).show()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_History -> Toast.makeText(this, "History Clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_bookmark -> Toast.makeText(this, "Bookmark Clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> {
                    Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Sort alphabetically by expertise before display
        val sortedWorkers = allWorkers.sortedBy { it.expertise.lowercase() }

        // Initial display
        loadWorkerProfiles(sortedWorkers)

        // Live Search
        searchDropdown.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val filtered = if (query.isEmpty()) {
                    sortedWorkers
                } else {
                    sortedWorkers.filter { it.expertise.lowercase().contains(query) }
                }
                loadWorkerProfiles(filtered)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    @SuppressLint("SetTextI18n", "UseKtx")
    private fun loadWorkerProfiles(workers: List<Worker>) {
        profileContainer.removeAllViews()

        if (workers.isEmpty()) {
            noResultsText.visibility = View.VISIBLE
            return
        } else {
            noResultsText.visibility = View.GONE
        }

        val sharedPref = getSharedPreferences("reviews_prefs", MODE_PRIVATE)

        for (worker in workers) {
            val workerCard = layoutInflater.inflate(R.layout.worker_card, profileContainer, false)

            workerCard.findViewById<TextView>(R.id.workerName).text = "Name: ${worker.name}"
            workerCard.findViewById<TextView>(R.id.workerContact).text = "Contact: ${worker.contact}"
            workerCard.findViewById<TextView>(R.id.workerExpertise).text = "Expertise: ${worker.expertise}"
            workerCard.findViewById<TextView>(R.id.workerExperience).text = "Experience: ${worker.experience}"

            val imageView = workerCard.findViewById<ImageView>(R.id.workerImage)
            if (worker.imageUri.isNotEmpty()) {
                imageView.setImageURI(Uri.parse(worker.imageUri))
            } else {
                imageView.setImageResource(R.drawable.person)
            }

            val reportCount = sharedPref.getInt("report_${worker.name}", 0)
            if (reportCount >= 5) {
                workerCard.setBackgroundColor(Color.parseColor("#A90A0A"))
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

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes") { dialog, _ ->
                    dialog.dismiss()
                    finishAffinity() // Closes the app completely
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}