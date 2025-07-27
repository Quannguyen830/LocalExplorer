package com.example.localexplorer.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// Define sealed class for routes to ensure type-safety and centralize paths
sealed class Screen(val route: String) {
    object PlacesList : Screen("places_list")
    object PlaceDetail : Screen("place_detail/{placeId}") {
        // Function to create the route with a specific place ID
        fun createRoute(placeId: String) = "place_detail/$placeId"
    }
    object MapView : Screen("map_view")
    object FavoritesList : Screen("favorites_list")
    object AboutApp : Screen("about_app") // Added to ensure more than 3 screens are covered
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PlacesList.route, // Our starting screen
        modifier = modifier
    ) {
        // Define destinations for each screen. These will be replaced by actual screens later.
        composable(Screen.PlacesList.route) {
            DummyScreen(name = "Places List Screen")
        }
        composable(Screen.PlaceDetail.route) { backStackEntry ->
            // Extract arguments like placeId from the route
            val placeId = backStackEntry.arguments?.getString("placeId")
            if (placeId != null) {
                DummyScreen(name = "Place Detail Screen for ID: $placeId")
            } else {
                // Handle error or navigate back if ID is missing (e.g., if navigated without ID)
                DummyScreen(name = "Error: Place ID Missing (Nav)")
            }
        }
        composable(Screen.MapView.route) {
            DummyScreen(name = "Map View Screen")
        }
        composable(Screen.FavoritesList.route) {
            DummyScreen(name = "Favorites List Screen")
        }
        composable(Screen.AboutApp.route) {
            DummyScreen(name = "About App Screen")
        }
    }
}

// A simple Composable for demonstration, to be replaced by actual screens
@Composable
fun DummyScreen(name: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to", fontSize = 20.sp)
        Text(text = name, fontSize = 24.sp)
    }
}