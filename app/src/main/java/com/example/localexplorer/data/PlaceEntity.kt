package com.example.localexplorer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey 
    val id: String,
    val name: String,
    val description: String?,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?,
    val category: String,
    val isFavorite: Boolean = false
) 