package com.example.localexplorer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.localexplorer.data.PlaceRepository
import com.example.localexplorer.data.MockDataProvider
import com.example.localexplorer.domain.Place
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlacesViewModel(
    private val repository: PlaceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("All")
    
    init {
        loadPlaces()
        seedDataIfEmpty()
    }
    
    private fun loadPlaces() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                combine(
                    repository.getAllPlaces(),
                    _searchQuery,
                    _selectedCategory
                ) { places, query, category ->
                    filterPlaces(places, query, category)
                }.collect { filteredPlaces ->
                    _uiState.value = _uiState.value.copy(
                        places = filteredPlaces,
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
    
    private fun filterPlaces(places: List<Place>, query: String, category: String): List<Place> {
        return places.filter { place ->
            val matchesSearch = query.isEmpty() || 
                place.name.contains(query, ignoreCase = true) ||
                place.description?.contains(query, ignoreCase = true) == true
            
            val matchesCategory = category == "All" || place.category == category
            
            matchesSearch && matchesCategory
        }
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
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
    
    private fun seedDataIfEmpty() {
        viewModelScope.launch {
            try {
                val places = repository.getAllPlaces().first()
                if (places.isEmpty()) {
                    repository.insertPlaces(MockDataProvider.getMockPlaces())
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to seed initial data: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun getCurrentSearchQuery(): String = _searchQuery.value
    fun getCurrentCategory(): String = _selectedCategory.value
    fun getCategories(): List<String> = MockDataProvider.getCategories()
}

data class PlacesUiState(
    val places: List<Place> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class PlacesViewModelFactory(
    private val repository: PlaceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlacesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 