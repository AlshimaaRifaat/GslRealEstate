package com.example.gslrealestate.domain.usecase

import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.domain.model.Result
import com.example.gslrealestate.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all listings
 * Following Single Responsibility Principle - handles only listing retrieval logic
 * 
 * @param repository The listing repository interface
 */
class GetListingsUseCase @Inject constructor(
    private val repository: ListingRepository
) {
    /**
     * Executes the use case to get all listings
     * @return Flow emitting Result with list of Listing
     */
    operator fun invoke(): Flow<Result<List<Listing>>> {
        return repository.getListings()
    }
}

