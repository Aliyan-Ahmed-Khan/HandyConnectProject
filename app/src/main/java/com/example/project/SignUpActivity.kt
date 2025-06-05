package com.example.project

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import java.security.MessageDigest
import androidx.appcompat.app.AppCompatActivity

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

    private lateinit var dbHelper: UserDatabaseHelper

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        dbHelper = UserDatabaseHelper(this)

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


        // Handling the selected user type shenanigans
        setFormEnabled(false)
        userTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (hasSpinnerInitialized) {
                    val isEnabled = position != 0
                    setFormEnabled(isEnabled)
                    if (!isEnabled) {
                        Toast.makeText(this@SignUpActivity, "Please select your field", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    hasSpinnerInitialized = true
                }

                val selectedType = parent?.getItemAtPosition(position).toString()
                when (selectedType) {
                    "Seeker" -> {
                        setFormEnabled(true)
                        expertiseEditText.visibility = View.GONE
                        experienceEditText.visibility = View.GONE
                    }

                    "Worker" -> {
                        setFormEnabled(true)
                        expertiseEditText.visibility = View.VISIBLE
                        experienceEditText.visibility = View.VISIBLE
                    }
                    else -> {
                        setFormEnabled(false)
                        expertiseEditText.visibility = View.GONE
                        experienceEditText.visibility = View.GONE
                    }
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
                    val email = emailEditText.text.toString()
                    val cnic = cnicEditText.text.toString()
                    val contact = contactEditText.text.toString()

                    if (isUserUnique(email, cnic, contact)) {
                        saveUserData()
                        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        imagePickerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
        }
    }

    // Enabling the fields after the user type has been selected
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
        radioMale.isClickable = enabled
        radioFemale.isEnabled = enabled
        imagePickerButton.isEnabled = enabled
    }

    // Validating the necessary values of the fields
    private fun validateFields(): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val cnicPattern = Regex("^\\d{13}$")
        val contactPattern = Regex("^\\d{11}$")

        if (nameEditText.text.isNullOrBlank()) {
            return errorField(nameEditText, "Full Name is required")
        }

        if (emailEditText.text.isNullOrBlank()) {
            return errorField(emailEditText, "Email is required")
        }

        if (passwordEditText.text.toString().length < 6) {
            return errorField(passwordEditText, "Password must be at least 6 characters")
        }

        if (!CnicVerificationService.isCnicVerified(cnicEditText.text.toString())) {
            return errorField(cnicEditText, "CNIC is not verified. Please enter a valid CNIC.")
        }

        val selectedGenderId = genderRadioGroup.checkedRadioButtonId
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            return false
        }

        val resolver = contentResolver
        try {
            resolver.openInputStream(imageUri!!)?.close()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load image, please try again", Toast.LENGTH_SHORT).show()
            return false
        }

        val selectedUserType = userTypeSpinner.selectedItem.toString()

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
            selectedUserType == "Worker" && expertiseEditText.text.isNullOrBlank() ->
                errorField(expertiseEditText, "Field of Expertise is required")
            selectedUserType == "Worker" && experienceEditText.text.isNullOrBlank() ->
                errorField(experienceEditText, "Experience is required")
            else -> true
        }
    }

    private fun errorField(field: EditText, message: String): Boolean {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        field.setBackgroundColor(Color.parseColor("#FFCDD2"))
        field.requestFocus()
        return false
    }

    // Hashing the password that was entered for security purposes
    private fun hashPassword(password: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }


    private fun isUserUnique(email: String, cnic: String, contact: String): Boolean {
        val db = dbHelper.readableDatabase

        val duplicateFields = mutableListOf<String>()

        val emailCursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
        if (emailCursor.moveToFirst()) {
            duplicateFields.add("Email")
        }
        emailCursor.close()

        val cnicCursor = db.rawQuery("SELECT * FROM users WHERE cnic = ?", arrayOf(cnic))
        if (cnicCursor.moveToFirst()) {
            duplicateFields.add("CNIC")
        }
        cnicCursor.close()

        val contactCursor = db.rawQuery("SELECT * FROM users WHERE contact = ?", arrayOf(contact))
        if (contactCursor.moveToFirst()) {
            duplicateFields.add("Contact")
        }
        contactCursor.close()

        db.close()

        return if (duplicateFields.isEmpty()) {
            true
        } else {
            val message = "${duplicateFields.joinToString(", ")} already exist${if (duplicateFields.size > 1) "!" else "s!"}"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun saveUserData() {
        val userType = userTypeSpinner.selectedItem.toString()
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val cnic = cnicEditText.text.toString()
        val contact = contactEditText.text.toString()
        val location = locationEditText.text.toString()
        val expertise = expertiseEditText.text.toString()
        val experience = experienceEditText.text.toString()
        val gender = if (radioMale.isChecked) "Male" else "Female"
        val image = imageUri?.toString()

        // 1. Save to database (existing code)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("userType", userType)
            put("name", name)
            put("email", email)
            put("password", hashPassword(password)) // hashed
            put("cnic", cnic)
            put("contact", contact)
            put("location", location)
            put("expertise", expertise)
            put("experience", experience)
            put("gender", gender)
            put("imageUri", image)
        }
        db.insert("users", null, values)
        db.close()

        // 2. Save to local list (new code)
//        LocalUserStore.userList.add(
//            SignUpUser(
//                userType,
//                name,
//                email,
//                password, // plain text here — you can also hash it if you prefer
//                cnic,
//                contact,
//                location,
//                if (userType == "Worker") expertise else null,
//                if (userType == "Worker") experience else null,
//                gender,
//                image
//            )
//        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
        }
    }
}
