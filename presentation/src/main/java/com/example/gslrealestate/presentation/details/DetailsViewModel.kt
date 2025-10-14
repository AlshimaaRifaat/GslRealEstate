package com.example.gslrealestate.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gslrealestate.domain.model.Result
import com.example.gslrealestate.domain.usecase.GetListingDetailsUseCase
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
 * ViewModel for Details screen following MVI pattern
 * Manages UI state and handles user intents
 * 
 * @param getListingDetailsUseCase Use case for retrieving listing details
 * @param savedStateHandle Saved state handle for navigation arguments
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getListingDetailsUseCase: GetListingDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsContract.State())
    val state: StateFlow<DetailsContract.State> = _state.asStateFlow()

    private val _events = Channel<DetailsContract.Event>()
    val events = _events.receiveAsFlow()

    init {
        val listingId = savedStateHandle.get<Int>("listingId")
        listingId?.let {
            handleIntent(DetailsContract.Intent.LoadListingDetails(it))
        }
    }

    /**
     * Handles user intents
     * Following Single Responsibility Principle
     */
    fun handleIntent(intent: DetailsContract.Intent) {
        when (intent) {
            is DetailsContract.Intent.LoadListingDetails -> loadListingDetails(intent.listingId)
            is DetailsContract.Intent.NavigateBack -> navigateBack()
        }
    }

    /**
     * Loads listing details from the use case
     */
    private fun loadListingDetails(listingId: Int) {
        viewModelScope.launch {
            getListingDetailsUseCase(listingId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                listing = result.data,
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
                            DetailsContract.Event.ShowError(
                                result.message ?: "An error occurred"
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * Triggers navigation back
     */
    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailsContract.Event.NavigateBack)
        }
    }
}

