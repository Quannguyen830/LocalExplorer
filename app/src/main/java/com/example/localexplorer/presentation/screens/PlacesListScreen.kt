package com.example.localexplorer.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.localexplorer.data.AppDatabase
import com.example.localexplorer.data.PlaceRepository
import com.example.localexplorer.presentation.components.CategoryFilter
import com.example.localexplorer.presentation.components.PlaceCard
import com.example.localexplorer.presentation.components.SearchBar
import com.example.localexplorer.presentation.viewmodel.PlacesViewModel
import com.example.localexplorer.presentation.viewmodel.PlacesViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesListScreen(
    onPlaceClick: (String) -> Unit,
    onMapClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val repository = PlaceRepository(database.placeDao())
    
    val viewModel: PlacesViewModel = viewModel(
        factory = PlacesViewModelFactory(repository)
    )
    
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover Places") },
                actions = {
                    IconButton(onClick = onFavoritesClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onMapClick) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Map View",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = viewModel.getCurrentSearchQuery(),
                onQueryChanged = viewModel::onSearchQueryChanged,
                modifier = Modifier.padding(16.dp)
            )

            CategoryFilter(
                categories = viewModel.getCategories(),
                selectedCategory = viewModel.getCurrentCategory(),
                onCategorySelected = viewModel::onCategorySelected,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Oops! Something went wrong",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.errorMessage ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = viewModel::clearError) {
                                Text("Try Again")
                            }
                        }
                    }
                }
                
                uiState.places.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No places found",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Try adjusting your search or category filter",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.places, key = { it.id }) { place ->
                            PlaceCard(
                                place = place,
                                onPlaceClick = onPlaceClick,
                                onFavoriteClick = viewModel::onFavoriteToggled
                            )
                        }
                    }
                }
            }
        }
    }
} 