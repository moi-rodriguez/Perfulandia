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
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val avatarUri: Uri? = null
)

/**
 * ViewModel de perfil actualizado.
 * Inyecta AuthRepository y AvatarRepository.
 */
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val avatarRepository: AvatarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadSavedAvatar()
        loadUserProfile()
    }

    private fun loadSavedAvatar() {
        viewModelScope.launch {
            avatarRepository.getAvatarUri().collect { savedUri: Uri? ->
                _uiState.update { it.copy(avatarUri = savedUri) }
            }
        }
    }

    fun loadUserProfile() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            // Usamos el AuthRepository que ya devuelve el usuario limpio
            val result = authRepository.getProfile()

            _uiState.update { current ->
                result.fold(
                    onSuccess = { userDomainModel ->
                        current.copy(
                            isLoading = false,
                            user = userDomainModel,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        val friendlyMessage = when (exception) {
                            is HttpException -> {
                                when (exception.code()) {
                                    401, 403 -> "Sesión no válida. Inicia sesión nuevamente."
                                    404 -> "No se pudo cargar tu perfil."
                                    500 -> "Error en el servidor."
                                    else -> "Error al cargar el perfil."
                                }
                            }
                            is UnknownHostException -> "No hay conexión a internet."
                            is SocketTimeoutException -> "El servidor tardó demasiado."
                            else -> exception.localizedMessage ?: "Error desconocido."
                        }

                        current.copy(
                            isLoading = false,
                            user = null,
                            error = friendlyMessage
                        )
                    }
                )
            }
        }
    }

    fun updateAvatar(uri: Uri?) {
        viewModelScope.launch {
            avatarRepository.saveAvatarUri(uri)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            avatarRepository.clearAvatarUri()
            _uiState.value = ProfileUiState(
                isLoading = false,
                user = null,
                error = null,
                avatarUri = null
            )
        }
    }
}