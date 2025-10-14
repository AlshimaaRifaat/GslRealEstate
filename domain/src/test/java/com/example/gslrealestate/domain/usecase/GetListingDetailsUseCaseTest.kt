package com.example.gslrealestate.domain.usecase

import app.cash.turbine.test
import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.domain.model.Result
import com.example.gslrealestate.domain.repository.ListingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for GetListingDetailsUseCase
 */
class GetListingDetailsUseCaseTest {

    private lateinit var useCase: GetListingDetailsUseCase
    private lateinit var repository: ListingRepository

    private val testListing = Listing(
        id = 1,
        city = "Paris",
        area = 120.0,
        price = 850000.0,
        professional = "GSL Agency",
        propertyType = "Apartment",
        offerType = 1
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetListingDetailsUseCase(repository)
    }

    @Test
    fun `invoke should call repository getListingDetails with correct id`() = runTest {
        // Given
        every { repository.getListingDetails(1) } returns flowOf(Result.Success(testListing))

        // When & Then
        useCase(1).test {
            val result = awaitItem()
            assertTrue(result is Result.Success)
            assertEquals(testListing, (result as Result.Success).data)
            awaitComplete()
        }

        verify { repository.getListingDetails(1) }
    }
}

