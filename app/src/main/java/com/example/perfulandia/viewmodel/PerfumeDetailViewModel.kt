package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.model.Perfume
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
    val error: String? = null,
    val navigateToLogin: Boolean = false, // Evento para navegar al login
    val showAddedToCartMessage: Boolean = false // Evento para mostrar mensaje
)

class PerfumeDetailViewModel(
    private val perfumeRepository: PerfumeRepository,
    private val sessionManager: SessionManager,
    private val cartViewModel: CartViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfumeDetailUiState())
    val uiState: StateFlow<PerfumeDetailUiState> = _uiState.asStateFlow()

    /**
     * Carga los detalles de un perfume específico por su ID.
     */
    fun loadPerfume(perfumeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = perfumeRepository.getPerfumeById(perfumeId)

            result.onSuccess { perfume ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        perfume = perfume
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar el perfume: ${error.message}"
                    )
                }
            }
        }
    }

    /**
     * Lógica que se ejecuta al presionar "Agregar al Carrito".
     */
    fun onAddToCartClicked() {
        viewModelScope.launch {
            val token = sessionManager.getAuthToken()
            if (token.isNullOrEmpty()) {
                // Usuario es invitado, necesita loguearse
                _uiState.update { it.copy(navigateToLogin = true) }
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