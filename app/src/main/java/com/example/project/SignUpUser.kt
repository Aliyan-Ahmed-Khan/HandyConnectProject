package com.example.project

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpUsers(
    val userType: String,
    val name: String,
    val email: String,
    val password: String,
    val cnic: String,
    val contact: String,
    val location: String,
    val expertise: String?,
    val experience: String?,
    val gender: String,
    val imageUri: String?
) : Parcelable
