package com.example.perfulandia.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.perfulandia.ui.screens.HomeScreen
import com.example.perfulandia.ui.screens.LoginScreen
import com.example.perfulandia.ui.screens.RegisterScreen
import com.example.perfulandia.ui.screens.ProfileScreen
import com.example.perfulandia.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    // El MainViewModel puede ser compartido si es necesario, o cada pantalla puede tener el suyo.
    // Para este caso, lo pasaremos a las pantallas que lo necesitan.
    val mainViewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,  // pantalla de inicio
        modifier = modifier
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = mainViewModel)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = mainViewModel)
        }
        composable(route = Screen.Register.route) {
             RegisterScreen(navController = navController)
         }
    }
}
