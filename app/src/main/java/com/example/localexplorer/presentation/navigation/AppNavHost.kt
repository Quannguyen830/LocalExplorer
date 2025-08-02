package com.example.localexplorer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.localexplorer.presentation.screens.PlacesListScreen
import com.example.localexplorer.presentation.screens.PlaceDetailScreen
import com.example.localexplorer.presentation.screens.MapScreen
import com.example.localexplorer.presentation.screens.FavoritesScreen

sealed class Screen(val route: String) {
    object PlacesList : Screen("places_list")
    object PlaceDetail : Screen("place_detail/{placeId}") {
        fun createRoute(placeId: String) = "place_detail/$placeId"
    }
    object MapView : Screen("map_view?lat={lat}&lng={lng}") {
        fun createRoute(latitude: Double? = null, longitude: Double? = null): String {
            return if (latitude != null && longitude != null) {
                "map_view?lat=$latitude&lng=$longitude"
            } else {
                "map_view"
            }
        }
    }
    object FavoritesList : Screen("favorites_list")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PlacesList.route,
        modifier = modifier
    ) {
        composable(Screen.PlacesList.route) {
            PlacesListScreen(
                onPlaceClick = { placeId ->
                    navController.navigate(Screen.PlaceDetail.createRoute(placeId))
                },
                onMapClick = {
                    navController.navigate(Screen.MapView.createRoute())
                },
                onFavoritesClick = {
                    navController.navigate(Screen.FavoritesList.route)
                }
            )
        }

        composable(
            route = Screen.PlaceDetail.route,
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString("placeId")
            if (placeId != null) {
                PlaceDetailScreen(
                    placeId = placeId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onMapClick = { latitude, longitude ->
                        navController.navigate(Screen.MapView.createRoute(latitude, longitude))
                    }
                )
            }
        }

        composable(
            route = Screen.MapView.route,
            arguments = listOf(
                navArgument("lat") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("lng") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val latString = backStackEntry.arguments?.getString("lat")
            val lngString = backStackEntry.arguments?.getString("lng")
            
            val latitude = latString?.toDoubleOrNull()
            val longitude = lngString?.toDoubleOrNull()
            
            MapScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPlaceClick = { placeId ->
                    navController.navigate(Screen.PlaceDetail.createRoute(placeId))
                },
                focusLatitude = latitude,
                focusLongitude = longitude
            )
        }

        composable(Screen.FavoritesList.route) {
            FavoritesScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPlaceClick = { placeId ->
                    navController.navigate(Screen.PlaceDetail.createRoute(placeId))
                }
            )
        }
    }
}