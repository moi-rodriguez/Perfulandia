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
 * Estado de la UI de Signup
 */
data class SignupUiState(
    val isLoading: Boolean = false,
    val isSignedUp: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel: Maneja la lógica de registro y el estado de la pantalla
 */
class SignupViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    // Estado PRIVADO (solo el ViewModel lo modifica)
    private val _uiState = MutableStateFlow(SignupUiState())

    // Estado PÚBLICO (la UI lo observa)
    val uiState: StateFlow<SignupUiState> = _uiState

    /**
     * Intenta registrar al usuario y guardar el token
     */
    fun signup(name: String, email: String, password: String) {
        // Indicar que está cargando y limpiar error previo
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null,
            isSignedUp = false
        )

        viewModelScope.launch {
            val result = repository.signup(name, email, password)

            _uiState.value = result.fold(
                onSuccess = {
                    // Éxito: usuario registrado y token guardado
                    _uiState.value.copy(
                        isLoading = false,
                        isSignedUp = true,
                        error = null
                    )
                },
                onFailure = { exception ->
                    val friendlyMessage = when (exception) {
                        is HttpException -> {
                            when (exception.code()) {
                                400 -> "Datos inválidos o correo ya registrado. Revisa la información."
                                401, 403 -> "No tienes permisos para registrarte con estos datos."
                                404 -> "Servicio no encontrado. Intenta más tarde."
                                429 -> "Demasiadas solicitudes. Espera un momento e inténtalo de nuevo."
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
                        isSignedUp = false,
                        error = friendlyMessage
                    )
                }
            )
        }
    }

    /**
     * Opcional: resetear el estado, por ejemplo después de navegar
     */
    fun resetState() {
        _uiState.value = SignupUiState()
    }
}
