package com.example.localexplorer.domain

// This is our core data model (entity) for a place.
// It's a simple data class to hold information about a location.
// It is deliberately simple and doesn't contain any Android or database specific annotations here.
data class Place(
    val id: String,         // Unique identifier for the place (e.g., from API or DB)
    val name: String,       // Name of the place (e.g., "Central Park", "Cafe Delight")
    val description: String?, // Optional detailed description
    val latitude: Double,   // Latitude coordinate (for maps)
    val longitude: Double,  // Longitude coordinate (for maps)
    val imageUrl: String?,  // Optional URL for an image of the place
    val category: String,   // Category (e.g., "Park", "Cafe", "Restaurant", "Museum")
    var isFavorite: Boolean = false // Whether the user has marked it as a favorite
)