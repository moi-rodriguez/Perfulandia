package com.example.perfulandia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Estado de la UI de Login
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel: Maneja la lógica de login y el estado de la pantalla
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null,
            isLoggedIn = false
        )

        viewModelScope.launch {
            val result = repository.login(email, password)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        error = null
                    )
                },
                onFailure = { exception ->
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

                    _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        error = friendlyMessage
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}
