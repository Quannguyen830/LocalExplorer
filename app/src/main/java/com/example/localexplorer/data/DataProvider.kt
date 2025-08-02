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
            try {
                fetchPlacesFromFoursquare()
            } catch (e: Exception) {
                getStaticFallbackPlaces()
            }
        }
    }
    
    private suspend fun fetchPlacesFromFoursquare(): List<Place> {
        val apiKey = BuildConfig.FOURSQUARE_API_KEY
        if (apiKey.isEmpty()) {
            throw Exception("Foursquare API key not configured")
        }
        
        val allPlaces = mutableListOf<Place>()
        
        FoursquareApiService.CATEGORY_MAPPINGS.forEach { (categoryName, categoryId) ->
            try {
                val response = foursquareService.searchPlaces(
                    authorization = apiKey,
                    latLng = FoursquareApiService.MELBOURNE_LAT_LNG,
                    categories = categoryId,
                    limit = 3
                )
                
                allPlaces.addAll(response.results.map { venue ->
                    mapFoursquareVenueToPlace(venue, categoryName)
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        return if (allPlaces.isNotEmpty()) allPlaces else getStaticFallbackPlaces()
    }
    
    private fun mapFoursquareVenueToPlace(venue: FoursquareVenue, categoryName: String): Place {
        val imageUrl = venue.photos?.firstOrNull()?.getImageUrl("800x600") 
            ?: getDefaultImageForCategory(categoryName)
        
        val description = venue.description 
            ?: venue.location.formattedAddress 
            ?: "Located in Melbourne"
        
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
    
    private fun getStaticFallbackPlaces(): List<Place> {
        return listOf(
            Place(
                id = "fallback_1",
                name = "Queen Victoria Market",
                description = "Historic market with fresh produce, gourmet foods, and unique souvenirs.",
                latitude = -37.8076,
                longitude = 144.9568,
                imageUrl = "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800",
                category = "Shopping"
            ),
            Place(
                id = "fallback_2",
                name = "Royal Botanic Gardens",
                description = "Beautiful botanical gardens with diverse plant collections and peaceful walking paths.",
                latitude = -37.8304,
                longitude = 144.9796,
                imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800",
                category = "Park"
            ),
            Place(
                id = "fallback_3",
                name = "Melbourne Museum",
                description = "Interactive museum showcasing natural and cultural history.",
                latitude = -37.8033,
                longitude = 144.9717,
                imageUrl = "https://images.unsplash.com/photo-1565035010268-a3816f98589a?w=800",
                category = "Museum"
            )
        )
    }
    
    fun getCategories(): List<String> {
        return listOf("All", "Café", "Restaurant", "Park", "Museum", "Shopping", "Entertainment")
    }
}