package com.example.gslrealestate.domain.usecase

import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.domain.model.Result
import com.example.gslrealestate.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving listing details by ID
 * Following Single Responsibility Principle - handles only detail retrieval logic
 * 
 * @param repository The listing repository interface
 */
class GetListingDetailsUseCase @Inject constructor(
    private val repository: ListingRepository
) {
    /**
     * Executes the use case to get listing details
     * @param listingId The ID of the listing to retrieve
     * @return Flow emitting Result with the Listing details
     */
    operator fun invoke(listingId: Int): Flow<Result<Listing>> {
        return repository.getListingDetails(listingId)
    }
}

