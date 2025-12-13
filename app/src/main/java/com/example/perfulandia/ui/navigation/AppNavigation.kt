package com.example.perfulandia.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.perfulandia.ui.screens.HomeScreen
import com.example.perfulandia.ui.screens.LoginScreen
import com.example.perfulandia.ui.screens.RegisterScreen
import com.example.perfulandia.ui.screens.ProfileScreen
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.ui.screens.AdminDashboardScreen
import com.example.perfulandia.ui.screens.ForgotPasswordScreen
import com.example.perfulandia.ui.screens.MyOrdersScreen
import com.example.perfulandia.ui.screens.MyReviewsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val context = LocalContext.current
    var startDestination by remember { mutableStateOf(Screen.Login.route) }
    var isCheckingSession by remember { mutableStateOf(true) }

    // al abrir la app, si hay sesion activa (token guardado) lleva directo a home,
    // y si no, por defecto a login
    LaunchedEffect(Unit) {
        val sessionManager = SessionManager(context)
        val token = sessionManager.getAuthToken()
        startDestination = if (token.isNullOrEmpty()) {
            Screen.Login.route
        } else {
            Screen.Home.route
        }
        kotlinx.coroutines.delay(2000)
        isCheckingSession = false
    }

    if (isCheckingSession) {
        // Pantalla de carga inicial
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
        ) {
            composable(route = Screen.Login.route) {
                LoginScreen(navController = navController)
            }
            composable(route = Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(route = Screen.Register.route) {
                RegisterScreen(navController = navController)
            }
            composable(route = Screen.AdminDashboard.route) {
                AdminDashboardScreen(navController = navController)
            }
            composable(route = Screen.ForgotPassword.route) {
                ForgotPasswordScreen(navController = navController)
            }
            composable(route = Screen.MyOrders.route) {
                MyOrdersScreen(navController = navController)
            }
            composable(route = Screen.MyReviews.route) {
                MyReviewsScreen(navController = navController)
            }
        }
    }
}
