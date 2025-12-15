package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.data.repository.ReviewRepository
import com.example.perfulandia.model.Perfume
import com.example.perfulandia.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la pantalla de detalle del perfume.
 */
data class PerfumeDetailUiState(
    val isLoading: Boolean = false,
    val perfume: Perfume? = null,
    val reviews: List<Review> = emptyList(), // Lista de reseñas
    val error: String? = null,
    val navigateToLogin: Boolean = false, // Evento para navegar al login
    val showAddedToCartMessage: Boolean = false, // Evento para mostrar mensaje
    val showLoginRequiredMessage: Boolean = false // Evento para mostrar mensaje de login requerido
)

class PerfumeDetailViewModel(
    private val perfumeRepository: PerfumeRepository,
    private val reviewRepository: ReviewRepository, // Inyectado
    private val sessionManager: SessionManager,
    private val cartViewModel: CartViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfumeDetailUiState())
    val uiState: StateFlow<PerfumeDetailUiState> = _uiState.asStateFlow()

    /**
     * Carga los detalles de un perfume específico y sus reseñas.
     */
    fun loadPerfume(perfumeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Cargar detalles del perfume
            val perfumeResult = perfumeRepository.getPerfumeById(perfumeId)
            perfumeResult.onSuccess { perfume ->
                _uiState.update { it.copy(perfume = perfume) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar el perfume: ${error.message}")
                }
                return@launch // Salir si el perfume falla
            }

            // Cargar reseñas del perfume
            val reviewsResult = reviewRepository.getReviewsByPerfumeId(perfumeId)
            reviewsResult.onSuccess { reviews ->
                _uiState.update { it.copy(isLoading = false, reviews = reviews) }
            }.onFailure { error ->
                // No tratar la falla de reseñas como un error fatal, solo mostrar el perfume
                _uiState.update { it.copy(isLoading = false) }
                // Opcionalmente, se podría registrar el error
            }
        }
    }

    /**
     * Lógica que se ejecuta al presionar "Agregar al Carrito".
     */
    fun onAddToCartClicked() {
        viewModelScope.launch {
            val userEmail = sessionManager.getUserEmail()
            if (userEmail == "invitado@sistema.com") {
                // Usuario es invitado, se cierra su sesión y se le pide que inicie sesión de nuevo.
                sessionManager.clearSession()
                _uiState.update { it.copy(showLoginRequiredMessage = true) }
            } else {
                // Usuario es cliente, agregar al carrito
                uiState.value.perfume?.let { perfume ->
                    cartViewModel.addToCart(perfume)
                    // Activar el mensaje de confirmación
                    _uiState.update { it.copy(showAddedToCartMessage = true) }
                }
            }
        }
    }

    /**
     * Resetea el evento de mensaje de login requerido para que no se muestre múltiples veces.
     */
    fun onLoginRequiredMessageShown() {
        _uiState.update { it.copy(showLoginRequiredMessage = false) }
    }

    /**
     * Solicita la navegación al login después de que el mensaje haya sido mostrado.
     */
    fun requestLoginNavigation() {
        _uiState.update { it.copy(navigateToLogin = true) }
    }

    /**
     * Resetea el evento de navegación para que no se dispare múltiples veces.
     */
    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateToLogin = false) }
    }

    /**
     * Resetea el evento de mensaje para que no se muestre múltiples veces.
     */
    fun onMessageShown() {
        _uiState.update { it.copy(showAddedToCartMessage = false) }
    }
}