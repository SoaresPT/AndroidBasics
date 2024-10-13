package com.example.parlament_app.data.model

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Data class representing additional information for an MP,
// including Twitter, birth year, and constituency.
data class ExtraMPData(
    val hetekaId: Int,
    val twitter: String?,
    val bornYear: Int?,
    val constituency: String?
)
