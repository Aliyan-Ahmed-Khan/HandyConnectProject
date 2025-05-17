package com.example.project

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var userTypeSpinner: Spinner
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userTypeSpinner = findViewById(R.id.userTypeSpinner)
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        signUpButton = findViewById(R.id.buttonSignUp)

        // Initially disable all fields
        setFormEnabled(false)

        userTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                // Enable form only if a valid user type is selected (not first index)
                setFormEnabled(position != 0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                setFormEnabled(false)
            }
        }

        signUpButton.setOnClickListener {
            val userType = userTypeSpinner.selectedItem.toString()
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // You can handle sign-up logic here
            Toast.makeText(this, "Registering $userType: $name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFormEnabled(enabled: Boolean) {
        nameEditText.isEnabled = enabled
        emailEditText.isEnabled = enabled
        passwordEditText.isEnabled = enabled
        signUpButton.isEnabled = enabled
    }
}
