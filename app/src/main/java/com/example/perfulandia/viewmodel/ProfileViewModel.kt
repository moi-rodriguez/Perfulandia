package com.example.perfulandia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.AppDependencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class User(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: Long
)

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val formattedCreatedAt: String = "",
    val avatarUri: Uri? = null
    // val imageUri: Uri? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val dependencies = AppDependencies.getInstance(application)

    // Obtener AvatarRepository del contenedor
    private val avatarRepository = dependencies.avatarRepository

    // _uiState es mutable y privado, solo el ViewModel puede modificarlo
    private val _uiState = MutableStateFlow(ProfileUiState())

    // uiState es público e inmutable, para ser observado desde la UI
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // loadUserProfile()
        loadSavedAvatar()
    }

    // cargar avatar desde DataStore
    private fun loadSavedAvatar() {
        viewModelScope.launch {
            avatarRepository.getAvatarUri().collect { savedUri: Uri? ->
                _uiState.update { it.copy(avatarUri = savedUri) }
            }
        }
    }


    /**
     * Actualiza la URI del avatar del usuario en el estado de la UI.
     * @param uri La nueva URI de la imagen seleccionada.
     */
    fun updateAvatar(uri: Uri?) {
        viewModelScope.launch {
            avatarRepository.saveAvatarUri(uri)
            // El estado se actualiza automáticamente vía Flow en loadSavedAvatar()
        }
    }
}
