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

    // state for map screen
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // just get all places and show on map
            repository.getAllPlaces().collect { places ->
                println("Loaded ${places.size} places for map") // debug
                _uiState.value = _uiState.value.copy(
                    places = places,
                    isLoading = false
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
            val place = repository.getPlaceById(placeId)
            place?.let {
                repository.updateFavoriteStatus(placeId, !it.isFavorite)
            }
        }
    }
}

data class MapUiState(
    val places: List<Place> = emptyList(),
    val selectedPlace: Place? = null,
    val isLoading: Boolean = false
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