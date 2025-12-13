package com.example.perfulandia.ui.navigation

// Sealed class para definir rutas tipo-safe en la navegación
sealed class Screen(val route: String) {
    // Rutas simples (sin argumentos): Usamos 'data object' es un singleton seguro de tipos

    data object Login : Screen("Login")
    data object Register : Screen("Register")
    data object Home : Screen("Home")
    data object Profile : Screen("Profile")
    data object AdminDashboard : Screen("AdminDashboard")
    data object ForgotPassword : Screen("ForgotPassword")
    data object MyOrders : Screen("MyOrders")
    data object MyReviews : Screen("MyReviews")
}