package com.example.gslrealestate.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.presentation.designsystem.components.ErrorMessage
import com.example.gslrealestate.presentation.designsystem.components.LoadingIndicator
import com.example.gslrealestate.presentation.designsystem.theme.GslRealEstateTheme
import com.example.gslrealestate.presentation.designsystem.theme.Spacing

/**
 * Details Screen - Displays detailed information about a specific property
 * Following Jetpack Compose best practices and MVI pattern
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is DetailsContract.Event.NavigateBack -> {
                    onNavigateBack()
                }
                is DetailsContract.Event.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Property Details") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleIntent(DetailsContract.Intent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        DetailsContent(
            state = state,
            onRetry = {
                state.listing?.let {
                    viewModel.handleIntent(DetailsContract.Intent.LoadListingDetails(it.id))
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun DetailsContent(
    state: DetailsContract.State,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            LoadingIndicator(modifier = modifier)
        }
        state.error != null && state.listing == null -> {
            ErrorMessage(
                message = state.error,
                onRetry = onRetry,
                modifier = modifier
            )
        }
        state.listing != null -> {
            ListingDetails(
                listing = state.listing,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ListingDetails(
    listing: Listing,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.medium)
    ) {
        // Property Image
        listing.url?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Property image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(Spacing.large))
        }

        // Property Title
        Text(
            text = listing.city,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        Text(
            text = listing.propertyType,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(Spacing.large))

        // Price Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(Spacing.medium)
            ) {
                Text(
                    text = "Price",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = listing.formattedPrice,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = listing.offerTypeDescription,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.large))

        // Property Details
        Text(
            text = "Property Details",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        DetailRow(label = "Area", value = listing.formattedArea)
        listing.rooms?.let {
            DetailRow(label = "Rooms", value = it.toString())
        }
        listing.bedrooms?.let {
            DetailRow(label = "Bedrooms", value = it.toString())
        }
        DetailRow(label = "Agency", value = listing.professional)

        Spacer(modifier = Modifier.height(Spacing.extraLarge))
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsScreenPreview() {
    GslRealEstateTheme {
        ListingDetails(
            listing = Listing(
                id = 1,
                city = "Paris",
                area = 120.0,
                price = 850000.0,
                professional = "GSL Agency",
                propertyType = "Luxury Apartment",
                offerType = 1,
                rooms = 4,
                bedrooms = 2,
                url = null
            )
        )
    }
}

