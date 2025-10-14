package com.example.gslrealestate.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Listing
 * Represents the JSON structure from the API
 * Following separation of concerns - DTOs are separate from domain models
 */
data class ListingDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("city")
    val city: String,
    @SerializedName("area")
    val area: Double,
    @SerializedName("price")
    val price: Double,
    @SerializedName("professional")
    val professional: String,
    @SerializedName("propertyType")
    val propertyType: String,
    @SerializedName("offerType")
    val offerType: Int,
    @SerializedName("bedrooms")
    val bedrooms: Int? = null,
    @SerializedName("rooms")
    val rooms: Int? = null,
    @SerializedName("url")
    val url: String? = null
)

