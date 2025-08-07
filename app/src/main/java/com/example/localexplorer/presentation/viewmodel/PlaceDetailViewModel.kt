package com.example.localexplorer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.localexplorer.data.PlaceRepository
import com.example.localexplorer.domain.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaceDetailViewModel(
    private val repository: PlaceRepository
) : ViewModel() {

    // state for place detail screen
    private val _uiState = MutableStateFlow(PlaceDetailUiState())
    val uiState: StateFlow<PlaceDetailUiState> = _uiState.asStateFlow()

    fun loadPlace(placeId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val place = repository.getPlaceById(placeId)
            if (place != null) {
                _uiState.value = _uiState.value.copy(
                    place = place,
                    isLoading = false
                )
            } else {
                // place not found - shouldn't happen but just in case
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    fun onFavoriteToggled() {
        val currentPlace = _uiState.value.place ?: return

        viewModelScope.launch {
            repository.updateFavoriteStatus(currentPlace.id, !currentPlace.isFavorite)

            // update UI immediately so it feels responsive
            _uiState.value = _uiState.value.copy(
                place = currentPlace.copy(isFavorite = !currentPlace.isFavorite)
            )
        }
    }
}

data class PlaceDetailUiState(
    val place: Place? = null,
    val isLoading: Boolean = false
)

class PlaceDetailViewModelFactory(
    private val repository: PlaceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaceDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 