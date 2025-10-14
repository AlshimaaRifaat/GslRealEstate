package com.example.gslrealestate.presentation.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.presentation.designsystem.theme.GslRealEstateTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for DetailsScreen
 * Following Jetpack Compose testing best practices
 */
class DetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testListing = Listing(
        id = 1,
        city = "Paris",
        area = 120.0,
        price = 850000.0,
        professional = "GSL Agency",
        propertyType = "Luxury Apartment",
        offerType = 1,
        rooms = 4,
        bedrooms = 2
    )

    @Test
    fun detailsScreen_displaysListingDetails() {
        // Given
        val viewModel = mockk<DetailsViewModel>(relaxed = true)
        val state = DetailsContract.State(
            isLoading = false,
            listing = testListing
        )
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.events } returns emptyFlow()

        // When
        composeTestRule.setContent {
            GslRealEstateTheme {
                DetailsScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Paris").assertIsDisplayed()
        composeTestRule.onNodeWithText("Luxury Apartment").assertIsDisplayed()
        composeTestRule.onNodeWithText("€850,000").assertIsDisplayed()
        composeTestRule.onNodeWithText("Property Details").assertIsDisplayed()
        composeTestRule.onNodeWithText("120 m²").assertIsDisplayed()
        composeTestRule.onNodeWithText("4").assertIsDisplayed() // Rooms
        composeTestRule.onNodeWithText("2").assertIsDisplayed() // Bedrooms
        composeTestRule.onNodeWithText("GSL Agency").assertIsDisplayed()
    }

    @Test
    fun detailsScreen_showsLoadingIndicator() {
        // Given
        val viewModel = mockk<DetailsViewModel>(relaxed = true)
        val state = DetailsContract.State(isLoading = true)
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.events } returns emptyFlow()

        // When
        composeTestRule.setContent {
            GslRealEstateTheme {
                DetailsScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then - Loading indicator should be displayed
        composeTestRule.onNodeWithText("Paris").assertDoesNotExist()
    }

    @Test
    fun detailsScreen_showsErrorMessage() {
        // Given
        val viewModel = mockk<DetailsViewModel>(relaxed = true)
        val state = DetailsContract.State(
            isLoading = false,
            error = "Listing not found"
        )
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.events } returns emptyFlow()

        // When
        composeTestRule.setContent {
            GslRealEstateTheme {
                DetailsScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Listing not found").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun detailsScreen_clickBackButton_triggersIntent() {
        // Given
        val viewModel = mockk<DetailsViewModel>(relaxed = true)
        val state = DetailsContract.State(
            isLoading = false,
            listing = testListing
        )
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.events } returns emptyFlow()
        every { viewModel.handleIntent(any()) } returns Unit

        // When
        composeTestRule.setContent {
            GslRealEstateTheme {
                DetailsScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Then
        verify { viewModel.handleIntent(DetailsContract.Intent.NavigateBack) }
    }
}

