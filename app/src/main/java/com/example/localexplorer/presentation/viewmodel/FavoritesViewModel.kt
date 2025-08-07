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

class FavoritesViewModel(
    private val repository: PlaceRepository
) : ViewModel() {

    // favorites screen state
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // get favorite places from database
            repository.getFavoritePlaces().collect { favorites ->
                println("Loaded ${favorites.size} favorites") // debug
                _uiState.value = _uiState.value.copy(
                    favorites = favorites,
                    isLoading = false
                )
            }
        }
    }
    
    fun onFavoriteRemoved(placeId: String) {
        viewModelScope.launch {
            // remove from favorites
            repository.updateFavoriteStatus(placeId, false)
        }
    }
}

data class FavoritesUiState(
    val favorites: List<Place> = emptyList(),
    val isLoading: Boolean = false
)

class FavoritesViewModelFactory(
    private val repository: PlaceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 