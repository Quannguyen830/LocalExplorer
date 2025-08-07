package com.example.localexplorer.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FoursquareApiService {

    // search places using foursquare API
    @GET("v3/places/search")
    suspend fun searchPlaces(
        @Header("Authorization") authorization: String,
        @Query("ll") latLng: String,
        @Query("categories") categories: String? = null,
        @Query("query") query: String? = null,
        @Query("radius") radius: Int = 10000, // 10km radius
        @Query("limit") limit: Int = 20,
        @Query("fields") fields: String = "fsq_id,name,location,categories,description,photos"
    ): FoursquareSearchResponse

    companion object {
        const val BASE_URL = "https://api.foursquare.com/"

        // category IDs from foursquare docs
        val CATEGORY_MAPPINGS = mapOf(
            "Café" to "13032",
            "Restaurant" to "13065",
            "Park" to "16032",
            "Museum" to "12026",
            "Shopping" to "17000",
            "Entertainment" to "10000"
        )

        // melbourne coordinates - hardcoded for now
        const val MELBOURNE_LAT_LNG = "-37.8136,144.9631"
    }
}