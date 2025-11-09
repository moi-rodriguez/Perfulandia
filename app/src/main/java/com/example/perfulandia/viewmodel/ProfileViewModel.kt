package com.example.perfulandia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.AppDependencies
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

// Modelo de usuario que usará la UI
data class User(
    val id: String,
    val name: String,
    val email: String
)

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val avatarUri: Uri? = null
)

/**
 * ViewModel de perfil:
 * - Carga datos remotos (/auth/me)
 * - Maneja avatar local (DataStore)
 * - Permite cerrar sesión (borra token + avatar)
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val dependencies = AppDependencies.getInstance(application)

    // Repositorio de avatar desde el contenedor
    private val avatarRepository = dependencies.avatarRepository

    // Repositorio de usuario (API)
    private val userRepository = UserRepository(application)

    // SessionManager para borrar la sesión al cerrar sesión
    private val sessionManager = SessionManager(application)

    // Estado privado mutable
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Estado público observado por la UI
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadSavedAvatar()
        loadUserProfile()
    }

    /**
     * Carga avatar guardado en DataStore
     */
    private fun loadSavedAvatar() {
        viewModelScope.launch {
            avatarRepository.getAvatarUri().collect { savedUri: Uri? ->
                _uiState.update { it.copy(avatarUri = savedUri) }
            }
        }
    }

    /**
     * Llama a /auth/me para obtener los datos del usuario actual
     */
    fun loadUserProfile() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = userRepository.getCurrentUser()

            _uiState.update { current ->
                result.fold(
                    onSuccess = { dto ->
                        val user = User(
                            id = dto.id.toString(),
                            name = dto.name,
                            email = dto.email
                        )

                        current.copy(
                            isLoading = false,
                            user = user,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        val friendlyMessage = when (exception) {
                            is HttpException -> {
                                when (exception.code()) {
                                    401, 403 -> "Sesión no válida. Inicia sesión nuevamente."
                                    404 -> "No se pudo cargar tu perfil."
                                    500 -> "Error en el servidor. Intenta más tarde."
                                    else -> "Error al cargar el perfil (${exception.code()})."
                                }
                            }
                            is UnknownHostException ->
                                "No hay conexión a internet. Revisa tu red."
                            is SocketTimeoutException ->
                                "El servidor tardó demasiado en responder."
                            else ->
                                exception.localizedMessage ?: "Ocurrió un error al cargar el perfil."
                        }

                        current.copy(
                            isLoading = false,
                            error = friendlyMessage
                        )
                    }
                )
            }
        }
    }

    /**
     * Actualiza el avatar del usuario y lo guarda en DataStore
     */
    fun updateAvatar(uri: Uri?) {
        viewModelScope.launch {
            avatarRepository.saveAvatarUri(uri)
            // El estado de avatarUri se actualiza solo vía el Flow de loadSavedAvatar()
        }
    }

    /**
     * CERRAR SESIÓN:
     * - Borra token y user_id (SessionManager)
     * - Borra avatar guardado
     * - Limpia el estado del perfil
     */
    fun logout() {
        viewModelScope.launch {
            // Borrar token + user_id
            sessionManager.clearSession()
            // Borrar avatar persistido
            avatarRepository.clearAvatarUri()

            // Limpiar estado en la UI
            _uiState.value = ProfileUiState(
                isLoading = false,
                user = null,
                error = null,
                avatarUri = null
            )
        }
    }
}
