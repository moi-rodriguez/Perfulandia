package com.example.perfulandia.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.data.repository.AvatarRepository
import com.example.perfulandia.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val avatarUri: Uri? = null,
    val error: String? = null,
    val isLoggedOut: Boolean = false
)

/**
 * ViewModel para la pantalla de Perfil.
 * Gestiona la carga de datos del usuario, actualización de avatar y cierre de sesión.
 */
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val avatarRepository: AvatarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    /**
     * Carga la información del usuario y su avatar.
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // 1. Cargar Avatar local
            val savedAvatar = avatarRepository.getAvatarUri().first()

            // 2. Cargar Datos del Usuario (Perfil)
            // Intentamos obtener el perfil fresco de la API, si falla, usamos los datos de sesión local
            val result = authRepository.getProfile()

            result.onSuccess { user ->
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    user = user,
                    avatarUri = savedAvatar
                )
            }.onFailure { e ->
                // Fallback: Intentar recuperar datos básicos de la sesión si la API falla
                val name = authRepository.getUserName()
                val email = authRepository.getUserEmail()

                if (name != null && email != null) {
                    // Creamos un usuario temporal con los datos locales
                    val localUser = User(id = "", nombre = name, email = email, role = "user")
                    _uiState.value = ProfileUiState(
                        isLoading = false,
                        user = localUser,
                        avatarUri = savedAvatar,
                        error = "Modo offline: No se pudo actualizar el perfil completo."
                    )
                } else {
                    _uiState.value = ProfileUiState(isLoading = false, error = e.message)
                }
            }
        }
    }

    /**
     * Actualiza el avatar seleccionado por el usuario.
     */
    fun updateAvatar(uri: Uri?) {
        viewModelScope.launch {
            avatarRepository.saveAvatarUri(uri)
            _uiState.value = _uiState.value.copy(avatarUri = uri)
        }
    }

    /**
     * Cierra la sesión del usuario y limpia datos locales.
     */
    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                avatarRepository.clearAvatarUri() // Opcional: limpiar avatar al salir
                _uiState.value = _uiState.value.copy(isLoggedOut = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error al cerrar sesión")
            }
        }
    }
}