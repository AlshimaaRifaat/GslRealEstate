package com.example.gslrealestate.presentation.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.domain.model.Result
import com.example.gslrealestate.domain.usecase.GetListingDetailsUseCase
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
 * Unit tests for DetailsViewModel
 * Following TDD principles and testing MVI pattern
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var getListingDetailsUseCase: GetListingDetailsUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private val testDispatcher = StandardTestDispatcher()

    private val testListing = Listing(
        id = 1,
        city = "Paris",
        area = 120.0,
        price = 850000.0,
        professional = "GSL Agency",
        propertyType = "Apartment",
        offerType = 1,
        rooms = 4,
        bedrooms = 2
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getListingDetailsUseCase = mockk()
        savedStateHandle = SavedStateHandle(mapOf("listingId" to 1))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel is initialized, should load listing details successfully`() = runTest {
        // Given
        every { getListingDetailsUseCase(1) } returns flowOf(
            Result.Loading,
            Result.Success(testListing)
        )

        // When
        viewModel = DetailsViewModel(getListingDetailsUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(testListing, state.listing)
            assertEquals(null, state.error)
        }
        verify { getListingDetailsUseCase(1) }
    }

    @Test
    fun `when loading listing details fails, should show error state`() = runTest {
        // Given
        val errorMessage = "Not found"
        every { getListingDetailsUseCase(1) } returns flowOf(
            Result.Loading,
            Result.Error(Exception(errorMessage), errorMessage)
        )

        // When
        viewModel = DetailsViewModel(getListingDetailsUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(null, state.listing)
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `when navigate back intent is triggered, should emit navigation event`() = runTest {
        // Given
        every { getListingDetailsUseCase(1) } returns flowOf(Result.Success(testListing))
        viewModel = DetailsViewModel(getListingDetailsUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.events.test {
            viewModel.handleIntent(DetailsContract.Intent.NavigateBack)
            
            // Then
            val event = awaitItem()
            assertTrue(event is DetailsContract.Event.NavigateBack)
        }
    }
}

