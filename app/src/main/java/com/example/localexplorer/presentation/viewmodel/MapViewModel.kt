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

class MapViewModel(
    private val repository: PlaceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()
    
    init {
        loadPlaces()
    }
    
    private fun loadPlaces() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                repository.getAllPlaces().collect { places ->
                    _uiState.value = _uiState.value.copy(
                        places = places,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load places: ${e.message}"
                )
            }
        }
    }
    
    fun onPlaceSelected(place: Place) {
        _uiState.value = _uiState.value.copy(selectedPlace = place)
    }
    
    fun clearSelectedPlace() {
        _uiState.value = _uiState.value.copy(selectedPlace = null)
    }
    
    fun onFavoriteToggled(placeId: String) {
        viewModelScope.launch {
            try {
                val place = repository.getPlaceById(placeId)
                place?.let {
                    repository.updateFavoriteStatus(placeId, !it.isFavorite)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update favorite: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class MapUiState(
    val places: List<Place> = emptyList(),
    val selectedPlace: Place? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MapViewModelFactory(
    private val repository: PlaceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 