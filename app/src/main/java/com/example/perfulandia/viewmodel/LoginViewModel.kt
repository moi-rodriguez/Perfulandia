package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Importamos el Repositorio y las clases de dominio/datos
import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Estado de la UI de Login
 * @param user El objeto User (modelo de dominio) en caso de éxito del login. Null si falla o está en carga.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

/**
 * ViewModel: Maneja la lógica de login y el estado de la pantalla.
 * Ahora inyecta AuthRepository, desacoplando la lógica de la capa de datos.
 */
class LoginViewModel(
    // 1. INYECCIÓN DE DEPENDENCIA: Usamos el Repositorio de la capa de dominio/datos
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {

        // 2. Preparamos el estado de carga
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null,
            user = null
        )

        viewModelScope.launch {

            // 3. Llamamos al Repositorio, que devuelve Result<User>.
            // NOTA: Se llama directamente con email y password, tal como está definido en AuthRepository.
            val result = authRepository.login(email, password)

            // 4. Manejamos el resultado y actualizamos el StateFlow
            val newState = result.fold(
                onSuccess = { userObject ->
                    LoginUiState(
                        isLoading = false,
                        user = userObject, // Éxito: Guardamos el modelo de dominio User
                        error = null
                    )
                },
                onFailure = { exception ->
                    // Lógica robusta de manejo de errores, manteniendo tu estructura original
                    val friendlyMessage = when (exception) {
                        is HttpException -> {
                            when (exception.code()) {
                                400 -> "Datos inválidos. Revisa el correo y la contraseña."
                                401, 403 -> "Usuario o contraseña incorrectos."
                                404 -> "Servicio no encontrado. Intenta nuevamente más tarde."
                                429 -> "Demasiados intentos. Espera un momento e inténtalo otra vez."
                                500 -> "Error en el servidor. Intenta nuevamente más tarde."
                                else -> "Error inesperado (${exception.code()}). Intenta de nuevo."
                            }
                        }
                        is UnknownHostException ->
                            "No hay conexión a internet. Revisa tu red e inténtalo otra vez."
                        is SocketTimeoutException ->
                            "El servidor tardó demasiado en responder. Intenta de nuevo."
                        else ->
                            exception.localizedMessage ?: "Ocurrió un error desconocido."
                    }

                    LoginUiState(
                        isLoading = false,
                        user = null,
                        error = friendlyMessage
                    )
                }
            )
            _uiState.value = newState
        }
    }

    /**
     * Función para limpiar el estado después de un éxito o error manejado en la UI.
     */
    fun resetState() {
        _uiState.value = LoginUiState()
    }
}