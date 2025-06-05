package com.example.project

object CnicVerificationService {

    // Simulated backend CNIC database (can be replaced with real API call)
    private val verifiedCnicList = listOf(
        "1111111111111",
        "2222222222222",
        "4250100000000"
    )

    // Method to verify CNIC
    fun isCnicVerified(cnic: String): Boolean {
        return verifiedCnicList.contains(cnic)
    }
}