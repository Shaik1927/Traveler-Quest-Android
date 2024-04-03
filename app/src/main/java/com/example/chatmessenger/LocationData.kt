package com.example.chatmessenger

data class LocationData(
    val id: String? = null,
    val activity: String? = null,
    val myLocation: String? = null,
    val advLocation: String? = null,
    val imageUrl: String? = null,  // Add the imageUrl field
    val latitude: Double? = null,  // Add latitude field
    val longitude: Double? = null  // Add longitude field
)
