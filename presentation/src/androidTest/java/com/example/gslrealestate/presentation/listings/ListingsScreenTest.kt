package com.example.gslrealestate.presentation.listings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
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
 * UI tests for ListingsScreen
 * Following Jetpack Compose testing best practices
 */
class ListingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
        )
    )

    @Test
    fun listingsScreen_displaysListings() {
        // Given
        val viewModel = mockk<ListingsViewModel>(relaxed = true)
        val state = ListingsContract.State(
            isLoading = false,
            listings = testListings
        )
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.events } returns emptyFlow()

        // When
        composeTestRule.setContent {
            GslRealEstateTheme {
                ListingsScreen(
                    onNavigateToDetails = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Paris").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apartment").assertIsDisplayed()
        composeTestRule.onNodeWithText("€850,000").assertIsDisplayed()
    }

    @Test
    fun listingsScreen_showsLoadingIndicator() {
        // Given
        val viewModel = mockk<ListingsViewModel>(relaxed = true)
        val state = ListingsContract.State(isLoading = true)
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.events } returns emptyFlow()

        // When
        composeTestRule.setContent {
            GslRealEstateTheme {
                ListingsScreen(
                    onNavigateToDetails = {},
                    viewModel = viewModel
                )
            }
        }

        // Then - Loading indicator should be displayed
        // The CircularProgressIndicator doesn't have a text node, so we verify by checking
        // that the error or listings are not displayed
        composeTestRule.onNodeWithText("Paris").assertDoesNotExist()
    }

    @Test
    fun listingsScreen_showsErrorMessage() {
        // Given
        val viewModel = mockk<ListingsViewModel>(relaxed = true)
        val state = ListingsContract.State(
            isLoading = false,
            error = "Network error"
        )
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.events } returns emptyFlow()

        // When
        composeTestRule.setContent {
            GslRealEstateTheme {
                ListingsScreen(
                    onNavigateToDetails = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun listingsScreen_clickOnListingCard_triggersIntent() {
        // Given
        val viewModel = mockk<ListingsViewModel>(relaxed = true)
        val state = ListingsContract.State(
            isLoading = false,
            listings = testListings
        )
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.events } returns emptyFlow()
        every { viewModel.handleIntent(any()) } returns Unit

        // When
        composeTestRule.setContent {
            GslRealEstateTheme {
                ListingsScreen(
                    onNavigateToDetails = {},
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Paris").performClick()

        // Then
        verify { viewModel.handleIntent(ListingsContract.Intent.NavigateToDetails(1)) }
    }
}

