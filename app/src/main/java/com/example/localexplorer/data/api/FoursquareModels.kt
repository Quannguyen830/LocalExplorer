package com.example.localexplorer.data.api

import com.google.gson.annotations.SerializedName

data class FoursquareSearchResponse(
    @SerializedName("results")
    val results: List<FoursquareVenue>
)

data class FoursquareVenue(
    @SerializedName("fsq_id")
    val fsqId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("location")
    val location: FoursquareLocation,
    @SerializedName("categories")
    val categories: List<FoursquareCategory>,
    @SerializedName("description")
    val description: String?,
    @SerializedName("photos")
    val photos: List<FoursquarePhoto>? = null
)

data class FoursquareLocation(
    @SerializedName("address")
    val address: String?,
    @SerializedName("locality")
    val locality: String?,
    @SerializedName("region")
    val region: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("formatted_address")
    val formattedAddress: String?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)

data class FoursquareCategory(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("short_name")
    val shortName: String?,
    @SerializedName("plural_name")
    val pluralName: String?,
    @SerializedName("icon")
    val icon: FoursquareIcon?
)

data class FoursquareIcon(
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("suffix")
    val suffix: String
)

data class FoursquarePhoto(
    @SerializedName("id")
    val id: String,
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("suffix")
    val suffix: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
) {
    fun getImageUrl(size: String = "800x600"): String {
        return "${prefix}${size}${suffix}"
    }
}