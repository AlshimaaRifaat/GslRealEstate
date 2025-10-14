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
 * Unit tests for GetListingsUseCase
 */
class GetListingsUseCaseTest {

    private lateinit var useCase: GetListingsUseCase
    private lateinit var repository: ListingRepository

    private val testListings = listOf(
        Listing(
            id = 1,
            city = "Paris",
            area = 120.0,
            price = 850000.0,
            professional = "GSL Agency",
            propertyType = "Apartment",
            offerType = 1
        )
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetListingsUseCase(repository)
    }

    @Test
    fun `invoke should call repository getListings`() = runTest {
        // Given
        every { repository.getListings() } returns flowOf(Result.Success(testListings))

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result is Result.Success)
            assertEquals(testListings, (result as Result.Success).data)
            awaitComplete()
        }

        verify { repository.getListings() }
    }
}

