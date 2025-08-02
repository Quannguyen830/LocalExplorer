package com.example.localexplorer.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.localexplorer.data.AppDatabase
import com.example.localexplorer.data.PlaceRepository
import com.example.localexplorer.presentation.viewmodel.MapViewModel
import com.example.localexplorer.presentation.viewmodel.MapViewModelFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBackClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    focusLatitude: Double? = null,
    focusLongitude: Double? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val repository = PlaceRepository(database.placeDao())
    
    val viewModel: MapViewModel = viewModel(
        factory = MapViewModelFactory(repository)
    )
    
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    val defaultPosition = LatLng(-37.8136, 144.9631)
    val focusPosition = if (focusLatitude != null && focusLongitude != null) {
        LatLng(focusLatitude, focusLongitude)
    } else {
        defaultPosition
    }
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(focusPosition, 12f)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Places Map") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                                text = "Error loading map",
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
                
                else -> {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        // Add markers for each place
                        uiState.places.forEach { place ->
                            Marker(
                                state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                                title = place.name,
                                snippet = place.category,
                                onClick = {
                                    viewModel.onPlaceSelected(place)
                                    true // Consume the click event
                                }
                            )
                        }
                    }
                    
                    // Show place details card when a place is selected
                    uiState.selectedPlace?.let { selectedPlace ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.BottomCenter),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // Category Badge
                                        Surface(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = selectedPlace.category,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.height(8.dp))
                                        
                                        Text(
                                            text = selectedPlace.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        
                                        if (selectedPlace.description != null) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = selectedPlace.description,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    
                                    // Favorite button
                                    IconButton(
                                        onClick = { viewModel.onFavoriteToggled(selectedPlace.id) }
                                    ) {
                                        Icon(
                                            imageVector = if (selectedPlace.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = if (selectedPlace.isFavorite) "Remove from favorites" else "Add to favorites",
                                            tint = if (selectedPlace.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { onPlaceClick(selectedPlace.id) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("View Details")
                                    }
                                    
                                    OutlinedButton(
                                        onClick = { viewModel.clearSelectedPlace() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Close")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 