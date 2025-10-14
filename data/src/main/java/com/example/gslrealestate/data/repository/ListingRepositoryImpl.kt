package com.example.gslrealestate.data.repository

import com.example.gslrealestate.data.mapper.ListingMapper
import com.example.gslrealestate.data.remote.api.ListingApiService
import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.domain.model.Result
import com.example.gslrealestate.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of ListingRepository
 * Following Dependency Inversion Principle - implements interface defined in domain layer
 * 
 * @param apiService The Retrofit API service
 */
class ListingRepositoryImpl @Inject constructor(
    private val apiService: ListingApiService
) : ListingRepository {

    /**
     * Fetches all listings from the remote API
     * Implements reactive programming with Flow
     * @return Flow emitting Result with list of Listing
     */
    override fun getListings(): Flow<Result<List<Listing>>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.getListings()
            val listings = ListingMapper.toDomainList(response.items)
            emit(Result.Success(listings))
        } catch (e: Exception) {
            emit(Result.Error(e, e.message))
        }
    }

    /**
     * Fetches a specific listing by its ID from the remote API
     * @param listingId The ID of the listing to fetch
     * @return Flow emitting Result with the Listing details
     */
    override fun getListingDetails(listingId: Int): Flow<Result<Listing>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.getListingDetails(listingId)
            val listing = ListingMapper.toDomain(response)
            emit(Result.Success(listing))
        } catch (e: Exception) {
            emit(Result.Error(e, e.message))
        }
    }
}

