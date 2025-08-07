package com.example.localexplorer.data

import com.example.localexplorer.BuildConfig
import com.example.localexplorer.data.api.FoursquareApiService
import com.example.localexplorer.data.api.FoursquareVenue
import com.example.localexplorer.domain.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DataProvider {
    
    private val foursquareService by lazy {
        Retrofit.Builder()
            .baseUrl(FoursquareApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoursquareApiService::class.java)
    }
    
    suspend fun getPlaces(): List<Place> {
        return withContext(Dispatchers.IO) {
            // just try to get from foursquare, if it fails return empty list
            try {
                fetchPlacesFromFoursquare()
            } catch (e: Exception) {
                println("API failed: ${e.message}") // debug
                emptyList() // return empty if API fails
            }
        }
    }
    
    private suspend fun fetchPlacesFromFoursquare(): List<Place> {
        val apiKey = BuildConfig.FOURSQUARE_API_KEY
        if (apiKey.isEmpty()) {
            throw Exception("API key not set")
        }

        val allPlaces = mutableListOf<Place>()

        // get places for each category - this might be slow but whatever
        FoursquareApiService.CATEGORY_MAPPINGS.forEach { (categoryName, categoryId) ->
            val response = foursquareService.searchPlaces(
                authorization = apiKey,
                latLng = FoursquareApiService.MELBOURNE_LAT_LNG, // hardcoded melbourne for now
                categories = categoryId,
                limit = 3
            )

            allPlaces.addAll(response.results.map { venue ->
                mapFoursquareVenueToPlace(venue, categoryName)
            })
        }

        return allPlaces
    }
    
    private fun mapFoursquareVenueToPlace(venue: FoursquareVenue, categoryName: String): Place {
        // get image from venue or use default
        val imageUrl = venue.photos?.firstOrNull()?.getImageUrl("800x600")
            ?: getDefaultImageForCategory(categoryName)

        val description = venue.description ?: venue.location.formattedAddress ?: "No description"

        return Place(
            id = venue.fsqId,
            name = venue.name,
            description = description,
            latitude = venue.location.latitude,
            longitude = venue.location.longitude,
            imageUrl = imageUrl,
            category = categoryName
        )
    }
    
    private fun getDefaultImageForCategory(category: String): String {
        return when (category) {
            "Café" -> "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800"
            "Restaurant" -> "https://images.unsplash.com/photo-1514933651103-005eec06c04b?w=800"
            "Park" -> "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800"
            "Museum" -> "https://images.unsplash.com/photo-1565035010268-a3816f98589a?w=800"
            "Shopping" -> "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800"
            "Entertainment" -> "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800"
            else -> "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800"
        }
    }
    

    
    fun getCategories(): List<String> {
        return listOf("All", "Café", "Restaurant", "Park", "Museum", "Shopping", "Entertainment")
    }
}