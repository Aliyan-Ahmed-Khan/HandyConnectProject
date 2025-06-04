package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.*
import java.security.MessageDigest

import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var userTypeSpinner: Spinner
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var dbHelper: UserDatabaseHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        userTypeSpinner = findViewById(R.id.userTypeSpinner)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        signInButton = findViewById(R.id.buttonSignIn)
        dbHelper = UserDatabaseHelper(this)

        setFormEnabled(false)

        userTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                setFormEnabled(position != 0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                setFormEnabled(false)
            }
        }

        signInButton.setOnClickListener {
            val userType = userTypeSpinner.selectedItem.toString()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (userTypeSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select a user type first", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                if (checkCredentials(userType, email, password)) {
                    Toast.makeText(this, "Login successful as $userType", Toast.LENGTH_SHORT).show()

                    // Retrieve user details
                    val userInfo = getUserInfo(userType, email, password)
                    if (userInfo != null) {
                        // Save logged-in username to SharedPreferences
                        val sharedPref = getSharedPreferences("reviews_prefs", MODE_PRIVATE)
                        sharedPref.edit().putString("logged_in_username", userInfo.name).apply()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("userName", userInfo.name)
                        intent.putExtra("userEmail", userInfo.email)
                        intent.putExtra("userType", userInfo.userType)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Making sure the hashed password is converted back so that it runs appropriately
    private fun hashPassword(password: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    private fun setFormEnabled(enabled: Boolean) {
        emailEditText.isEnabled = enabled
        passwordEditText.isEnabled = enabled
    }

    private fun checkCredentials(userType: String, email: String, password: String): Boolean {
        val hashedPassword = hashPassword(password)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE userType = ? AND email = ? AND password = ?",
            arrayOf(userType, email, hashedPassword)
        )
        val isValid = cursor.moveToFirst()
        cursor.close()
        return isValid
    }

    private fun getUserInfo(userType: String, email: String, password: String): User? {
        val hashedPassword = hashPassword(password)
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM users WHERE userType = ? AND email = ? AND password = ?",
            arrayOf(userType, email, hashedPassword)
        )
        var user: User? = null
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex("name")
            val emailIndex = cursor.getColumnIndex("email")
            val userTypeIndex = cursor.getColumnIndex("userType")
            if (nameIndex != -1 && emailIndex != -1 && userTypeIndex != -1) {
                val name = cursor.getString(nameIndex)
                val emailFromDB = cursor.getString(emailIndex)
                val type = cursor.getString(userTypeIndex)
                user = User(name, emailFromDB, type)
            }
        }
        cursor.close()
        return user
    }

    data class User(val name: String, val email: String, val userType: String)
}