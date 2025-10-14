package com.example.gslrealestate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.gslrealestate.navigation.NavGraph
import com.example.gslrealestate.presentation.designsystem.theme.GslRealEstateTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for GSL Real Estate application
 * Annotated with @AndroidEntryPoint to enable Hilt dependency injection
 * Following Clean Architecture and MVI pattern
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GslRealEstateTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}