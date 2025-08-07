package com.example.localexplorer.domain

// main data class for places
data class Place(
    val id: String,
    val name: String,
    val description: String?,
    val latitude: Double, // lat/lng for map
    val longitude: Double,
    val imageUrl: String?,
    val category: String,
    var isFavorite: Boolean = false // mutable for favorites
)