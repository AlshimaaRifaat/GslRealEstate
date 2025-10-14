package com.example.gslrealestate.presentation.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gslrealestate.domain.model.Result
import com.example.gslrealestate.domain.usecase.GetListingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Listings screen following MVI pattern
 * Manages UI state and handles user intents
 * 
 * @param getListingsUseCase Use case for retrieving listings
 */
@HiltViewModel
class ListingsViewModel @Inject constructor(
    private val getListingsUseCase: GetListingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListingsContract.State())
    val state: StateFlow<ListingsContract.State> = _state.asStateFlow()

    private val _events = Channel<ListingsContract.Event>()
    val events = _events.receiveAsFlow()

    init {
        handleIntent(ListingsContract.Intent.LoadListings)
    }

    /**
     * Handles user intents
     * Following Single Responsibility Principle
     */
    fun handleIntent(intent: ListingsContract.Intent) {
        when (intent) {
            is ListingsContract.Intent.LoadListings -> loadListings()
            is ListingsContract.Intent.RefreshListings -> loadListings()
            is ListingsContract.Intent.NavigateToDetails -> navigateToDetails(intent.listingId)
        }
    }

    /**
     * Loads listings from the use case
     */
    private fun loadListings() {
        viewModelScope.launch {
            getListingsUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                listings = result.data,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "An error occurred"
                            )
                        }
                        _events.send(
                            ListingsContract.Event.ShowError(
                                result.message ?: "An error occurred"
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * Triggers navigation to details screen
     */
    private fun navigateToDetails(listingId: Int) {
        viewModelScope.launch {
            _events.send(ListingsContract.Event.NavigateToListingDetails(listingId))
        }
    }
}

