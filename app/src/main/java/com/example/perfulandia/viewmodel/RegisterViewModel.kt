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
 * Representa el estado de la interfaz de usuario en la pantalla de Registro.
 */
data class SignupUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null, // Error general de API
    val isSuccess: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)

/**
 * ViewModel encargado de la lógica de negocio para el registro de nuevos usuarios.
 */
class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    fun register(nombre: String, email: String, password: String) {
        var newState = _uiState.value.copy(
            nameError = null,
            emailError = null,
            passwordError = null,
            error = null
        )

        val isNameInvalid = nombre.isBlank()
        val isEmailInvalid = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordInvalid = password.length < 6

        if (isNameInvalid) {
            newState = newState.copy(nameError = "El nombre no puede estar vacío")
        }

        if (isEmailInvalid) {
            newState = newState.copy(emailError = "El formato del email no es válido")
        }

        if (isPasswordInvalid) {
            newState = newState.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
        }

        _uiState.value = newState

        if (isNameInvalid || isEmailInvalid || isPasswordInvalid) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = authRepository.register(nombre, email, password)

            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    isSuccess = true
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Error desconocido al registrar"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = SignupUiState()
    }
}