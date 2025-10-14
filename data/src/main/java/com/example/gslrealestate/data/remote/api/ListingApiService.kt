package com.example.gslrealestate.data.remote.api

import com.example.gslrealestate.data.remote.dto.ListingDto
import com.example.gslrealestate.data.remote.dto.ListingsResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API service interface for listing endpoints
 * Following Interface Segregation Principle - defines only necessary methods
 */
interface ListingApiService {
    /**
     * Fetches all listings from the API
     * @return ListingsResponse containing list of listings
     */
    @GET("listings.json")
    suspend fun getListings(): ListingsResponse

    /**
     * Fetches a specific listing by ID
     * @param listingId The ID of the listing
     * @return ListingDto containing listing details
     */
    @GET("listings/{listingId}.json")
    suspend fun getListingDetails(@Path("listingId") listingId: Int): ListingDto
}

