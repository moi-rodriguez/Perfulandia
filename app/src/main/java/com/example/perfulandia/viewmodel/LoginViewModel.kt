package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Patterns

/**
 * Estado de la UI de Login
 * @param user El objeto User (modelo de dominio) en caso de éxito del login. Null si falla o está en carga.
 * @param emailError Mensaje de error para el campo de email.
 * @param passwordError Mensaje de error para el campo de contraseña.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null, // Error general (ej: de API)
    val isSuccess: Boolean = false, // Flag útil para navegación
    val emailError: String? = null,
    val passwordError: String? = null
)

/**
 * ViewModel: Maneja la lógica de login y el estado de la pantalla.
 * Inyecta AuthRepository, desacoplando la lógica de la capa de datos.
 */
class LoginViewModel(
    // INYECCIÓN DE DEPENDENCIA: Usamos el Repositorio de la capa de dominio/datos
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState(emailError = null, passwordError = null))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        // Resetear errores de validación previos
        var newState = _uiState.value.copy(emailError = null, passwordError = null, error = null)

        // Validaciones
        val isEmailInvalid = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordInvalid = pass.length < 6

        if (isEmailInvalid) {
            newState = newState.copy(emailError = "El formato del email no es válido")
        }

        if (isPasswordInvalid) {
            newState = newState.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
        }

        _uiState.value = newState

        // Si hay errores de validación, no continuar
        if (isEmailInvalid || isPasswordInvalid) {
            return
        }

        viewModelScope.launch {
            // 1. Estado de carga
            _uiState.value = _uiState.value.copy(isLoading = true)

            // 2. Llamada al repositorio (devuelve Result<User>)
            val result = authRepository.login(email, pass)

            // 3. Manejo del resultado
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    isSuccess = true
                )
            }.onFailure { exception ->
                // El repositorio ya nos da un mensaje amigable (ej. "Credenciales inválidas")
                _uiState.value = _uiState.value.copy(
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
        _uiState.value = LoginUiState(emailError = null, passwordError = null)
    }
}