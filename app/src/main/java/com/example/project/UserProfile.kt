package com.example.project

import java.io.Serializable

data class User(
    val id: Int,
    val userType: String,
    val name: String,
    val email: String,
    val cnic: String,
    val contact: String,
    val location: String,
    val expertise: String,
    val experience: String,
    val gender: String,
    val imageUri: String
): Serializable
