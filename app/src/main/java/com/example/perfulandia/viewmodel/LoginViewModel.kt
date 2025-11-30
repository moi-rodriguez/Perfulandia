package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI de Login
 * @param user El objeto User (modelo de dominio) en caso de éxito del login. Null si falla o está en carga.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isSuccess: Boolean = false // Flag útil para navegación
)

/**
 * ViewModel: Maneja la lógica de login y el estado de la pantalla.
 * Inyecta AuthRepository, desacoplando la lógica de la capa de datos.
 */
class LoginViewModel(
    // INYECCIÓN DE DEPENDENCIA: Usamos el Repositorio de la capa de dominio/datos
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            // 1. Estado de carga
            _uiState.value = LoginUiState(isLoading = true)

            // 2. Llamada al repositorio (devuelve Result<User>)
            val result = authRepository.login(email, pass)

            // 3. Manejo del resultado
            result.onSuccess { user ->
                _uiState.value = LoginUiState(
                    isLoading = false,
                    user = user,
                    isSuccess = true,
                    error = null
                )
            }.onFailure { exception ->
                // El repositorio ya nos da un mensaje amigable (ej. "Credenciales inválidas")
                _uiState.value = LoginUiState(
                    isLoading = false,
                    error = exception.message ?: "Error desconocido"
                )
            }
        }
    }

    /**
     * Función para limpiar el estado después de un éxito o error manejado en la UI.
     */
    fun resetState() {
        _uiState.value = LoginUiState()
    }
}