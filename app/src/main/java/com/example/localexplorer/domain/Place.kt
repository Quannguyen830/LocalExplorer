package com.example.localexplorer.domain

data class Place(
    val id: String,
    val name: String,
    val description: String?,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?,
    val category: String,
    var isFavorite: Boolean = false
)