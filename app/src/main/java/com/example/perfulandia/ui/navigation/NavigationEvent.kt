package com.example.perfulandia.ui.navigation

// Representa los distintos tipos de eventos de navegación
sealed class NavigationEvent {
    /**
     * Evento para navegar a un destino específico.
     *
     * @param route         La AppDestinations (objeto de ruta tipado) a la que navegar.
     *
     * @param popUpToRoute  La ruta de destino en la pila de navegación hasta la cual se debe hacer 'pop',
     *                      pero si es null, no se hace 'pop' a un destino específico.
     *
     * @param inclusive     Si 'true', la ruta especificada en [popUpToRoute] también se elimina de la pila.
     *
     * @param singleTop     Si 'true', evita múltiples copias del mismo destino en la parte superior
     *                      de la pila si ya está presente (útil para navegación de barra inferior/lateral).
     */
    data class NavigateTo(
        val route: Screen,  // Ahora recibe un objeto AppDestinations (más seguro)
        val popUpToRoute: Screen? = null,  // También acepta un objeto AppDestinations
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    /**
     * Evento para volver a la pantalla anterior en la pila de navegación.
     */
    object PopBackStack : NavigationEvent()

    /**
     * Evento para navegar "hacia arriba" en la jerarquía de la aplicación.
     *
     * Generalmente es equivalente a [PopBackStack] a menos que se use
     * un grafo de navegación con una jerarquía padre-hijo definida.
     */
    object NavigateUp : NavigationEvent()
}