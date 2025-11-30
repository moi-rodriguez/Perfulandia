package com.example.perfulandia.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Importamos los Repositorios de la nueva arquitectura
import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.repository.AvatarRepository
// Importamos el Modelo de Dominio de Usuario
import com.example.perfulandia.model.User

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Estado de la UI de Perfil
 * @param user El modelo de dominio User. Contiene toda la información del usuario logueado.
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val avatarUri: Uri? = null
)

/**
 * ViewModel de perfil:
 * - Carga datos remotos (/auth/me) usando AuthRepository
 * - Maneja avatar local (DataStore) usando AvatarRepository
 * - Permite cerrar sesión (AuthRepository)
 * * *NOTA: Eliminamos AndroidViewModel y AppDependencies para usar inyección directa.*
 */
class ProfileViewModel(
    // 1. INYECCIÓN DE DEPENDENCIAS
    private val authRepository: AuthRepository,
    private val avatarRepository: AvatarRepository
) : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Estado público observado por la UI
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadSavedAvatar()
        loadUserProfile()
    }

    /**
     * Carga avatar guardado localmente (DataStore)
     */
    private fun loadSavedAvatar() {
        viewModelScope.launch {
            // Recolectamos el flujo del avatar y actualizamos el estado
            avatarRepository.getAvatarUri().collect { savedUri: Uri? ->
                _uiState.update { it.copy(avatarUri = savedUri) }
            }
        }
    }

    /**
     * Llama a authRepository.getProfile() para obtener los datos del usuario actual
     * *NOTA: El repositorio ahora devuelve el modelo de dominio User, simplificando el mapeo.*
     */
    fun loadUserProfile() {
        // Aseguramos que solo cargamos si el usuario tiene sesión activa, para evitar peticiones innecesarias
        viewModelScope.launch {
            if (!authRepository.isLoggedIn()) {
                _uiState.update { it.copy(isLoading = false, user = null, error = "Usuario no autenticado.") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.getProfile()

            _uiState.update { current ->
                result.fold(
                    onSuccess = { userDomainModel ->
                        // El repositorio ya devolvió el modelo de dominio 'User' limpio
                        current.copy(
                            isLoading = false,
                            user = userDomainModel, // Usamos el modelo de dominio directamente
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
                            // Si falla, el usuario debe ser null
                            user = null,
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
            // El estado de avatarUri se actualiza automáticamente vía el Flow de loadSavedAvatar()
        }
    }

    /**
     * CERRAR SESIÓN:
     * - Llama al repositorio para borrar token/user_id
     * - Borra avatar local
     * - Limpia el estado del perfil
     */
    fun logout() {
        viewModelScope.launch {
            // El AuthRepository ahora maneja la limpieza de la sesión (token, user_id)
            authRepository.logout()

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