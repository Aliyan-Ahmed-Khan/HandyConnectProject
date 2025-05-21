package com.example.project

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var userTypeSpinner: Spinner
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var cnicEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var expertiseEditText: EditText
    private lateinit var experienceEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var radioMale: RadioButton
    private lateinit var radioFemale: RadioButton
    private lateinit var imagePickerButton: Button
    private lateinit var profileImageView: ImageView

    private var hasSpinnerInitialized = false
    private var imageUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize views
        userTypeSpinner = findViewById(R.id.userTypeSpinner)
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        cnicEditText = findViewById(R.id.editTextCnic)
        contactEditText = findViewById(R.id.editTextContact)
        locationEditText = findViewById(R.id.editTextLocation)
        expertiseEditText = findViewById(R.id.editTextExpertise)
        experienceEditText = findViewById(R.id.editTextExperience)
        genderRadioGroup = findViewById(R.id.radioGroupGender)
        radioMale = findViewById(R.id.radioMale)
        radioFemale = findViewById(R.id.radioFemale)
        imagePickerButton = findViewById(R.id.buttonUploadImage)
        profileImageView = findViewById(R.id.profileImageView)
        signUpButton = findViewById(R.id.buttonSignUp)

        cnicEditText.hint = "e.g. 4212076850083"
        contactEditText.hint = "e.g. 03001234567"

        setFormEnabled(false)

        userTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (hasSpinnerInitialized) {
                    val isEnabled = position != 0
                    setFormEnabled(isEnabled)
                    if (!isEnabled) {
                        Toast.makeText(this@SignUpActivity, "Please select your field", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    hasSpinnerInitialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                setFormEnabled(false)
            }
        }

        val allEditTexts = listOf(
            nameEditText, emailEditText, passwordEditText, confirmPasswordEditText,
            cnicEditText, contactEditText, locationEditText, expertiseEditText, experienceEditText
        )

        allEditTexts.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    editText.setBackgroundColor(Color.TRANSPARENT)
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        signUpButton.setOnClickListener {
            if (userTypeSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select your field", Toast.LENGTH_SHORT).show()
            } else {
                if (validateFields()) {
                    Toast.makeText(this, "All fields validated successfully!", Toast.LENGTH_SHORT).show()
                    // Continue with registration
                }
            }
        }

        imagePickerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
        }
    }

    private fun setFormEnabled(enabled: Boolean) {
        nameEditText.isEnabled = enabled
        emailEditText.isEnabled = enabled
        passwordEditText.isEnabled = enabled
        confirmPasswordEditText.isEnabled = enabled
        cnicEditText.isEnabled = enabled
        contactEditText.isEnabled = enabled
        locationEditText.isEnabled = enabled
        expertiseEditText.isEnabled = enabled
        experienceEditText.isEnabled = enabled
        genderRadioGroup.isEnabled = enabled
        radioMale.isEnabled = enabled
        radioFemale.isEnabled = enabled
        imagePickerButton.isEnabled = enabled
    }

    private fun validateFields(): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val cnicPattern = Regex("^\\d{13}$")
        val contactPattern = Regex("^\\d{11}$")

        val selectedGenderId = genderRadioGroup.checkedRadioButtonId
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            return false
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please upload a profile image", Toast.LENGTH_SHORT).show()
            return false
        }

        return when {
            nameEditText.text.isNullOrBlank() -> errorField(nameEditText, "Full Name is required")
            emailEditText.text.isNullOrBlank() -> errorField(emailEditText, "Email is required")
            !emailPattern.matches(emailEditText.text.toString()) -> errorField(emailEditText, "Invalid Email Format")
            passwordEditText.text.isNullOrBlank() -> errorField(passwordEditText, "Password is required")
            confirmPasswordEditText.text.isNullOrBlank() -> errorField(confirmPasswordEditText, "Please confirm password")
            passwordEditText.text.toString() != confirmPasswordEditText.text.toString() ->
                errorField(confirmPasswordEditText, "Passwords do not match")
            cnicEditText.text.isNullOrBlank() -> errorField(cnicEditText, "CNIC is required")
            !cnicPattern.matches(cnicEditText.text.toString()) -> errorField(cnicEditText, "CNIC must be 13 digits")
            contactEditText.text.isNullOrBlank() -> errorField(contactEditText, "Contact Number is required")
            !contactPattern.matches(contactEditText.text.toString()) -> errorField(contactEditText, "Contact must be 11 digits")
            locationEditText.text.isNullOrBlank() -> errorField(locationEditText, "Location is required")
            expertiseEditText.text.isNullOrBlank() -> errorField(expertiseEditText, "Field of Expertise is required")
            experienceEditText.text.isNullOrBlank() -> errorField(experienceEditText, "Experience is required")
            else -> true
        }
    }

    private fun errorField(field: EditText, message: String): Boolean {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        field.setBackgroundColor(Color.parseColor("#FFCDD2"))
        field.requestFocus()
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
        }
    }
}
