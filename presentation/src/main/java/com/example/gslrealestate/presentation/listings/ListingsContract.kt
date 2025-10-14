package com.example.gslrealestate.presentation.listings

import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.presentation.common.UiEvent
import com.example.gslrealestate.presentation.common.UiIntent
import com.example.gslrealestate.presentation.common.UiState

/**
 * Contract defining MVI components for Listings screen
 * Following Interface Segregation Principle
 */
object ListingsContract {

    /**
     * UI State for Listings screen
     */
    data class State(
        val isLoading: Boolean = false,
        val listings: List<Listing> = emptyList(),
        val error: String? = null
    ) : UiState

    /**
     * User Intents/Actions for Listings screen
     */
    sealed class Intent : UiIntent {
        data object LoadListings : Intent()
        data object RefreshListings : Intent()
        data class NavigateToDetails(val listingId: Int) : Intent()
    }

    /**
     * One-time UI Events for Listings screen
     */
    sealed class Event : UiEvent {
        data class NavigateToListingDetails(val listingId: Int) : Event()
        data class ShowError(val message: String) : Event()
    }
}

