package com.example.gslrealestate.presentation.details

import com.example.gslrealestate.domain.model.Listing
import com.example.gslrealestate.presentation.common.UiEvent
import com.example.gslrealestate.presentation.common.UiIntent
import com.example.gslrealestate.presentation.common.UiState

/**
 * Contract defining MVI components for Details screen
 * Following Interface Segregation Principle
 */
object DetailsContract {

    /**
     * UI State for Details screen
     */
    data class State(
        val isLoading: Boolean = false,
        val listing: Listing? = null,
        val error: String? = null
    ) : UiState

    /**
     * User Intents/Actions for Details screen
     */
    sealed class Intent : UiIntent {
        data class LoadListingDetails(val listingId: Int) : Intent()
        data object NavigateBack : Intent()
    }

    /**
     * One-time UI Events for Details screen
     */
    sealed class Event : UiEvent {
        data object NavigateBack : Event()
        data class ShowError(val message: String) : Event()
    }
}

