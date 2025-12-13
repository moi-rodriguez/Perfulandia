package com.example.perfulandia.ui.navigation

/**
 * Sealed class para definir rutas tipo-safe en la navegación
 */
sealed class Screen(val route: String) {
    // Rutas Básicas
    data object Login : Screen("Login")
    data object Register : Screen("Register")
    data object ForgotPassword : Screen("ForgotPassword")
    data object Home : Screen("Home")
    data object Cart : Screen("Cart")
    data object OrderSuccess : Screen("OrderSuccess")
    data object Profile : Screen("Profile")
    data object MyOrders : Screen("MyOrders")
    data object CreatePerfume : Screen("CreatePerfume") // SOLO PARA ADMIN

    // Rutas con argumentos
    data object PerfumeDetail : Screen("PerfumeDetail/{perfumeId}") {
        fun createRoute(perfumeId: String) = "PerfumeDetail/$perfumeId"
    }
    data object CreateReview : Screen("CreateReview/{perfumeId}") {
        fun createRoute(perfumeId: String) = "CreateReview/$perfumeId"
    }
}