package com.example.project

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var userTypeSpinner: Spinner
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        userTypeSpinner = findViewById(R.id.userTypeSpinner)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        signInButton = findViewById(R.id.buttonSignIn)

        // Initially disable form
        setFormEnabled(false)

        userTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                // Enable fields only if user selects valid type
                setFormEnabled(position != 0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                setFormEnabled(false)
            }
        }


        signInButton.setOnClickListener {
            val userType = userTypeSpinner.selectedItem.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if(userTypeSpinner.selectedItemPosition == 0){
                Toast.makeText(this,"Please select a user type first", Toast.LENGTH_SHORT).show()
            }
            else {
                // Handle your login logic here
                Toast.makeText(this, "Logging in $userType: $email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFormEnabled(enabled: Boolean) {
        emailEditText.isEnabled = enabled
        passwordEditText.isEnabled = enabled

    }
}
