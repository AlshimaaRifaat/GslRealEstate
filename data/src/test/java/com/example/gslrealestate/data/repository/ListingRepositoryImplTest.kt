package com.example.gslrealestate.data.repository

import app.cash.turbine.test
import com.example.gslrealestate.data.remote.api.ListingApiService
import com.example.gslrealestate.data.remote.dto.ListingDto
import com.example.gslrealestate.data.remote.dto.ListingsResponse
import com.example.gslrealestate.domain.model.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ListingRepositoryImpl
 * Following TDD principles
 */
class ListingRepositoryImplTest {

    private lateinit var repository: ListingRepositoryImpl
    private lateinit var apiService: ListingApiService

    private val testListingDto = ListingDto(
        id = 1,
        city = "Paris",
        area = 120.0,
        price = 850000.0,
        professional = "GSL Agency",
        propertyType = "Apartment",
        offerType = 1,
        rooms = 4,
        bedrooms = 2,
        url = null
    )

    @Before
    fun setup() {
        apiService = mockk()
        repository = ListingRepositoryImpl(apiService)
    }

    @Test
    fun `getListings should emit loading then success`() = runTest {
        // Given
        val response = ListingsResponse(
            items = listOf(testListingDto),
            totalCount = 1
        )
        coEvery { apiService.getListings() } returns response

        // When & Then
        repository.getListings().test {
            // First emission should be Loading
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            // Second emission should be Success
            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertEquals(1, (success as Result.Success).data.size)
            assertEquals("Paris", success.data[0].city)

            awaitComplete()
        }
    }

    @Test
    fun `getListings should emit loading then error when API fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { apiService.getListings() } throws exception

        // When & Then
        repository.getListings().test {
            // First emission should be Loading
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            // Second emission should be Error
            val error = awaitItem()
            assertTrue(error is Result.Error)
            assertEquals("Network error", (error as Result.Error).message)

            awaitComplete()
        }
    }

    @Test
    fun `getListingDetails should emit loading then success`() = runTest {
        // Given
        coEvery { apiService.getListingDetails(1) } returns testListingDto

        // When & Then
        repository.getListingDetails(1).test {
            // First emission should be Loading
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            // Second emission should be Success
            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertEquals(1, (success as Result.Success).data.id)
            assertEquals("Paris", success.data.city)

            awaitComplete()
        }
    }

    @Test
    fun `getListingDetails should emit loading then error when API fails`() = runTest {
        // Given
        val exception = Exception("Not found")
        coEvery { apiService.getListingDetails(1) } throws exception

        // When & Then
        repository.getListingDetails(1).test {
            // First emission should be Loading
            val loading = awaitItem()
            assertTrue(loading is Result.Loading)

            // Second emission should be Error
            val error = awaitItem()
            assertTrue(error is Result.Error)
            assertEquals("Not found", (error as Result.Error).message)

            awaitComplete()
        }
    }
}

