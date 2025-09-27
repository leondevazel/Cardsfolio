package com.cs407.cardfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs407.cardfolio.ui.screen.HomeScreen
import com.cs407.cardfolio.ui.screen.AddCardScreen
import com.cs407.cardfolio.ui.screen.AllCardsScreen
import com.cs407.cardfolio.ui.screen.FavoriteScreen
import com.cs407.cardfolio.ui.theme.CardfolioTheme

// MainActivity is the entry point of the application
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables drawing behind system bars for a full-screen look
        enableEdgeToEdge()
        // Sets the UI content for this Activity using Jetpack Compose
        setContent {
            // Applies the app's theme
            CardfolioTheme {
                // Calls the AppNavigation composable to set up navigation
                AppNavigation()
            }
        }
    }
}

// Composable function responsible for navigation between screens
@Composable
fun AppNavigation() {
    // Creates and remembers a NavController to manage navigation state
    val navController = rememberNavController()

    // NavHost sets up the navigation graph for the app
    NavHost(
        navController = navController, // Controller that handles navigation
        startDestination = "home" // First screen to display when app starts
    ) {
        // Defines the "home" route and what UI to display there
        composable("home") {
            HomeScreen(
                onNavigateToAddCard = { navController.navigate("add_card") },
                onNavigateToAllCards = { navController.navigate("all_cards") },
                onNavigateToFavorites = { navController.navigate("favorites") }
            ) // Displays the HomeScreen composable
        }

        // Defines the "add_card" route
        composable("add_card") {
            AddCardScreen(
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        // Defines the "all_cards" route
        composable("all_cards") {
            AllCardsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Defines the "favorites" route
        composable("favorites") {
            FavoriteScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}