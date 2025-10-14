package com.example.gslrealestate.domain.repository

import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.domain.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining the contract for listing data operations
 * Following Dependency Inversion Principle - domain layer defines the interface,
 * data layer provides the implementation
 */
interface ListingRepository {
    /**
     * Fetches all listings from the remote source
     * @return Flow emitting Result with list of Listing
     */
    fun getListings(): Flow<Result<List<Listing>>>

    /**
     * Fetches a specific listing by its ID
     * @param listingId The ID of the listing to fetch
     * @return Flow emitting Result with the Listing details
     */
    fun getListingDetails(listingId: Int): Flow<Result<Listing>>
}

