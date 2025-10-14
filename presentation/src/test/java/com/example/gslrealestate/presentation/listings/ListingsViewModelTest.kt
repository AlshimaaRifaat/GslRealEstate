package com.example.gslrealestate.presentation.listings

import app.cash.turbine.test
import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.domain.model.Result
import com.example.gslrealestate.domain.usecase.GetListingsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ListingsViewModel
 * Following TDD principles and testing MVI pattern
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ListingsViewModelTest {

    private lateinit var viewModel: ListingsViewModel
    private lateinit var getListingsUseCase: GetListingsUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val testListings = listOf(
        Listing(
            id = 1,
            city = "Paris",
            area = 120.0,
            price = 850000.0,
            professional = "GSL Agency",
            propertyType = "Apartment",
            offerType = 1,
            rooms = 4,
            bedrooms = 2
        ),
        Listing(
            id = 2,
            city = "Lyon",
            area = 90.0,
            price = 450000.0,
            professional = "GSL Property",
            propertyType = "House",
            offerType = 2,
            rooms = 3,
            bedrooms = 2
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getListingsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel is initialized, should load listings successfully`() = runTest {
        // Given
        every { getListingsUseCase() } returns flowOf(
            Result.Loading,
            Result.Success(testListings)
        )

        // When
        viewModel = ListingsViewModel(getListingsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(testListings, state.listings)
            assertEquals(null, state.error)
        }
        verify { getListingsUseCase() }
    }

    @Test
    fun `when loading listings fails, should show error state`() = runTest {
        // Given
        val errorMessage = "Network error"
        every { getListingsUseCase() } returns flowOf(
            Result.Loading,
            Result.Error(Exception(errorMessage), errorMessage)
        )

        // When
        viewModel = ListingsViewModel(getListingsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.listings.isEmpty())
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `when refresh intent is triggered, should reload listings`() = runTest {
        // Given
        every { getListingsUseCase() } returns flowOf(
            Result.Loading,
            Result.Success(testListings)
        )
        viewModel = ListingsViewModel(getListingsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.handleIntent(ListingsContract.Intent.RefreshListings)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(exactly = 2) { getListingsUseCase() }
    }

    @Test
    fun `when navigate to details intent is triggered, should emit navigation event`() = runTest {
        // Given
        every { getListingsUseCase() } returns flowOf(Result.Success(testListings))
        viewModel = ListingsViewModel(getListingsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.events.test {
            viewModel.handleIntent(ListingsContract.Intent.NavigateToDetails(1))
            
            // Then
            val event = awaitItem()
            assertTrue(event is ListingsContract.Event.NavigateToListingDetails)
            assertEquals(1, (event as ListingsContract.Event.NavigateToListingDetails).listingId)
        }
    }
}

