package com.example.project

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object LocalUserStore {
    val userList = mutableListOf<SignUpUser>()
}
@Parcelize
data class SignUpUser(
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