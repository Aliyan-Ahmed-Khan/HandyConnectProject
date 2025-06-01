package com.example.project

data class UserProfile(
    val id: Int = 0,
    val userType: String,
    val name: String,
    val email: String,
    val password: String,
    val cnic: String,
    val contact: String,
    val location: String,
    val expertise: String,
    val experience: String,
    val gender: String,
    val imageUri: String
)
