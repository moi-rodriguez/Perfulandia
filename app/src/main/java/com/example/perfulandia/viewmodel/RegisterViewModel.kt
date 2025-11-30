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
 * Representa el estado de la interfaz de usuario en la pantalla de Registro.
 *
 * @property isLoading Indica si hay una operación de registro en curso.
 * @property user El usuario resultante si el registro fue exitoso.
 * @property error Mensaje de error en caso de fallo, o null si no hay error.
 * @property isSuccess Flag simple para indicar que el registro completó correctamente.
 */
data class SignupUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)

/**
 * ViewModel encargado de la lógica de negocio para el registro de nuevos usuarios.
 *
 * @property authRepository Repositorio encargado de la comunicación con la API de autenticación.
 */
class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Estado mutable interno (Backing property)
    private val _uiState = MutableStateFlow(SignupUiState())

    /**
     * Estado público inmutable que la UI observará.
     */
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    /**
     * Intenta registrar un nuevo usuario en el sistema.
     *
     * @param nombre El nombre completo del usuario.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña elegida por el usuario.
     */
    fun register(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            // 1. Emitimos estado de carga
            _uiState.value = SignupUiState(isLoading = true)

            // 2. Llamamos al repositorio de forma asíncrona
            val result = authRepository.register(nombre, email, password)

            // 3. Manejamos el resultado (Éxito o Fallo)
            result.onSuccess { user ->
                _uiState.value = SignupUiState(
                    isLoading = false,
                    user = user,
                    isSuccess = true,
                    error = null
                )
            }.onFailure { exception ->
                _uiState.value = SignupUiState(
                    isLoading = false,
                    error = exception.message ?: "Error desconocido al registrar"
                )
            }
        }
    }

    /**
     * Restablece el estado de la UI a sus valores iniciales.
     * Útil cuando se navega fuera de la pantalla y se regresa.
     */
    fun resetState() {
        _uiState.value = SignupUiState()
    }
}