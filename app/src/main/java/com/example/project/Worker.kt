package com.example.project

import java.io.Serializable

data class Worker(
    val name: String,
    val contact: String,
    val expertise: String,
    val experience: String,
    val imageUri: String,
    val reportCount: Int
) : Serializable