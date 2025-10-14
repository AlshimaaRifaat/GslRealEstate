package com.example.gslrealestate.presentation.listings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.presentation.designsystem.components.ErrorMessage
import com.example.gslrealestate.presentation.designsystem.components.LoadingIndicator
import com.example.gslrealestate.presentation.designsystem.components.PropertyCard
import com.example.gslrealestate.presentation.designsystem.theme.GslRealEstateTheme
import com.example.gslrealestate.presentation.designsystem.theme.Spacing

/**
 * Listings Screen - Main screen displaying list of real estate properties
 * Following Jetpack Compose best practices and MVI pattern
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingsScreen(
    onNavigateToDetails: (Int) -> Unit,
    viewModel: ListingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ListingsContract.Event.NavigateToListingDetails -> {
                    onNavigateToDetails(event.listingId)
                }
                is ListingsContract.Event.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Real Estate Listings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        ListingsContent(
            state = state,
            onListingClick = { listingId ->
                viewModel.handleIntent(ListingsContract.Intent.NavigateToDetails(listingId))
            },
            onRetry = {
                viewModel.handleIntent(ListingsContract.Intent.RefreshListings)
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun ListingsContent(
    state: ListingsContract.State,
    onListingClick: (Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            LoadingIndicator(modifier = modifier)
        }
        state.error != null && state.listings.isEmpty() -> {
            ErrorMessage(
                message = state.error,
                onRetry = onRetry,
                modifier = modifier
            )
        }
        state.listings.isEmpty() -> {
            ErrorMessage(
                message = "No listings available",
                modifier = modifier
            )
        }
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(Spacing.medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.medium)
            ) {
                items(
                    items = state.listings,
                    key = { it.id }
                ) { listing ->
                    PropertyCard(
                        listing = listing,
                        onClick = onListingClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListingsScreenPreview() {
    GslRealEstateTheme {
        ListingsContent(
            state = ListingsContract.State(
                isLoading = false,
                listings = listOf(
                    Listing(
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
                    ),
                    Listing(
                        id = 2,
                        city = "Lyon",
                        area = 90.0,
                        price = 450000.0,
                        professional = "GSL Property",
                        propertyType = "Apartment",
                        offerType = 1,
                        rooms = 3,
                        bedrooms = 2,
                        url = null
                    )
                )
            ),
            onListingClick = {},
            onRetry = {}
        )
    }
}

