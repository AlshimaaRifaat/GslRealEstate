package com.example.gslrealestate.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Listings Response
 * Represents the root JSON structure from the listings endpoint
 */
data class ListingsResponse(
    @SerializedName("items")
    val items: List<ListingDto>,
    @SerializedName("totalCount")
    val totalCount: Int
)

