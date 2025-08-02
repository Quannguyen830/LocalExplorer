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
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                repository.getFavoritePlaces().collect { favorites ->
                    _uiState.value = _uiState.value.copy(
                        favorites = favorites,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load favorites: ${e.message}"
                )
            }
        }
    }
    
    fun onFavoriteRemoved(placeId: String) {
        viewModelScope.launch {
            try {
                repository.updateFavoriteStatus(placeId, false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to remove favorite: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class FavoritesUiState(
    val favorites: List<Place> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
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