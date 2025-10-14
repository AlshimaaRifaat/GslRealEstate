package com.example.gslrealestate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gslrealestate.presentation.details.DetailsScreen
import com.example.gslrealestate.presentation.listings.ListingsScreen

/**
 * Navigation Graph for the application
 * Following Navigation Component best practices
 */

// Navigation Routes
sealed class Screen(val route: String) {
    data object Listings : Screen("listings")
    data object Details : Screen("details/{listingId}") {
        fun createRoute(listingId: Int) = "details/$listingId"
    }
}

/**
 * Main navigation host for the application
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Listings.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Listings Screen
        composable(route = Screen.Listings.route) {
            ListingsScreen(
                onNavigateToDetails = { listingId ->
                    navController.navigate(Screen.Details.createRoute(listingId))
                }
            )
        }

        // Details Screen
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument("listingId") {
                    type = NavType.IntType
                }
            )
        ) {
            DetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

