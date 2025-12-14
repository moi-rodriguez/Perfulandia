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
import com.example.perfulandia.ui.screens.CartScreen
import com.example.perfulandia.ui.screens.CreatePerfumeScreen
import com.example.perfulandia.ui.screens.CreateReviewScreen
import com.example.perfulandia.ui.screens.ForgotPasswordScreen
import com.example.perfulandia.ui.screens.MyOrdersScreen
import com.example.perfulandia.ui.screens.OrderSuccessScreen
import com.example.perfulandia.ui.screens.PerfumeDetailScreen

import com.example.perfulandia.AppDependencies
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perfulandia.viewmodel.HomeViewModel
import com.example.perfulandia.viewmodel.PerfumeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val appDependencies = remember { AppDependencies.getInstance(context) }

    // Estado para controlar la pantalla de inicio
    var startDestination by remember { mutableStateOf(Screen.Login.route) }
    var isCheckingSession by remember { mutableStateOf(true) }

    // Verificación de sesión al iniciar
    LaunchedEffect(Unit) {
        val sessionManager = SessionManager(context)
        val token = sessionManager.getAuthToken()

        // Si hay token, navega a Home. Si no, a Login
        startDestination = if (token.isNullOrEmpty()) {
            Screen.Login.route
        } else {
            Screen.Home.route
        }

        // Simula un pequeño delay y terminamos la carga
        kotlinx.coroutines.delay(2000)
        isCheckingSession = false
    }

    if (isCheckingSession) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                val homeViewModel: HomeViewModel = viewModel(factory = appDependencies.homeViewModelFactory)
                HomeScreen(navController = navController, viewModel = homeViewModel)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(route = Screen.Register.route) {
                RegisterScreen(navController = navController)
            }
            composable(route = Screen.ForgotPassword.route) {
                ForgotPasswordScreen(navController = navController)
            }
            composable(route = Screen.MyOrders.route) {
                MyOrdersScreen(navController = navController)
            }

            // PANTALLAS NUEVAS

            // Carrito y Compra
            composable(route = Screen.Cart.route) {
                CartScreen(navController = navController)
            }
            composable(route = Screen.OrderSuccess.route) {
                OrderSuccessScreen(navController = navController)
            }

            // Admin
            composable(route = Screen.CreatePerfume.route) {
                CreatePerfumeScreen(navController = navController)
            }

            // Rutas con Argumentos
            composable(route = Screen.PerfumeDetail.route) { backStackEntry ->
                val perfumeId = backStackEntry.arguments?.getString("perfumeId")
                if (perfumeId != null) {
                    val perfumeDetailViewModel: PerfumeDetailViewModel = viewModel(factory = appDependencies.perfumeDetailViewModelFactory)
                    PerfumeDetailScreen(
                        navController = navController,
                        perfumeId = perfumeId,
                        viewModel = perfumeDetailViewModel
                    )
                }
            }

            composable(route = Screen.CreateReview.route) { backStackEntry ->
                val perfumeId = backStackEntry.arguments?.getString("perfumeId")
                CreateReviewScreen(navController = navController, perfumeId = perfumeId)
            }
        }
    }
}
