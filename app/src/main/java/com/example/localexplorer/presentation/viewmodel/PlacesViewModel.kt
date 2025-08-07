package com.example.localexplorer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.localexplorer.data.PlaceRepository
import com.example.localexplorer.data.DataProvider
import com.example.localexplorer.domain.Place
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlacesViewModel(
    private val repository: PlaceRepository
) : ViewModel() {

    // UI state for the places screen
    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState.asStateFlow()

    // search and filter stuff
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("All")

    init {
        println("PlacesViewModel created") // debug
        loadPlaces()
        seedDataIfEmpty()
    }
    
    private fun loadPlaces() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // combine search and category filtering - learned this from stackoverflow
            combine(
                repository.getAllPlaces(),
                _searchQuery,
                _selectedCategory
            ) { places, query, category ->
                filterPlaces(places, query, category)
            }.collect { filteredPlaces ->
                _uiState.value = _uiState.value.copy(
                    places = filteredPlaces,
                    isLoading = false
                )
            }
        }
    }
    
    // filter function - probably could be optimized but works fine
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
            val place = repository.getPlaceById(placeId)
            place?.let {
                repository.updateFavoriteStatus(placeId, !it.isFavorite)
            }
        }
    }
    
    private fun seedDataIfEmpty() {
        viewModelScope.launch {
            val places = repository.getAllPlaces().first()
            if (places.isEmpty()) {
                // load initial data from API
                repository.insertPlaces(DataProvider.getPlaces())
                println("Seeded places from API") // debug
            }
        }
    }
    
    // helper functions for UI
    fun getCurrentSearchQuery(): String = _searchQuery.value
    fun getCurrentCategory(): String = _selectedCategory.value
    fun getCategories(): List<String> = DataProvider.getCategories()
}

data class PlacesUiState(
    val places: List<Place> = emptyList(),
    val isLoading: Boolean = false
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