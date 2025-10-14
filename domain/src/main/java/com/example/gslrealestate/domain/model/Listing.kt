package com.example.gslrealestate.domain.model

/**
 * Domain model representing a real estate listing
 * Following Clean Architecture principles - domain models are independent of frameworks
 */
data class Listing(
    val id: Int,
    val city: String,
    val area: Double,
    val price: Double,
    val professional: String,
    val propertyType: String,
    val offerType: Int,
    val bedrooms: Int? = null,
    val rooms: Int? = null,
    val url: String? = null
) {
    /**
     * Computed property for formatted price
     */
    val formattedPrice: String
        get() = "€${String.format("%,.0f", price)}"

    /**
     * Computed property for formatted area
     */
    val formattedArea: String
        get() = "${area.toInt()} m²"

    /**
     * Offer type description
     */
    val offerTypeDescription: String
        get() = when (offerType) {
            1 -> "For Sale"
            2 -> "For Rent"
            3 -> "Sold"
            else -> "Unknown"
        }
}

