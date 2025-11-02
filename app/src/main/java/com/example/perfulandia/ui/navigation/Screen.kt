package com.example.perfulandia.ui.navigation

import kotlin.text.replace

// Sealed class para definir rutas tipo-safe en la navegación
sealed class Screen(val route: String) {
    // Rutas simples (sin argumentos): Usamos 'data object' es un singleton seguro de tipos

    data object Home : Screen("home_page")          // Ruta a la pantalla de Inicio
    data object Profile : Screen("profile_page")    // Ruta a la pantalla de Perfil

    /*
        Ejemplos de Rutas con Argumentos (no usados en este ejercicio, pero importante)
        Usamos 'data class' para encapsular los argumentos.
        Esto asegura que, al navegar, los argumentos correctos sean pasados y tipados.
    */

    /**
     * Ejemplo de una ruta a una pantalla de detalles que requiere un 'ItemId'.
     *
     * @param itemId El ID del elemento a mostrar en la pantalla de detalles.
     */
    data class Detail(val itemId: String) : Screen("detail_page/{itemId}") {
        fun buildRoute(): String {                                                  // Función para construir la ruta final con el argumento. Para evitar errores al crear la ruta string.
            return route.replace("{itemId}", itemId)             // Reemplaza el placeholder "{itemId}" en la ruta base con el valor real.
        }
    }

    /*
        Si tuvieras más argumentos, se agregarían a la data class y a la cadena de ruta. Ejemplo:

            data class UserProfile(val userId: String, val userName: String?) :
                AppDestinations("profile/{userId?name={userName}") { ... }
     */
}