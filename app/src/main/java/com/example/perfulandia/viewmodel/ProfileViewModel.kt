// File: app/src/main/java/com/example/perfulandia/viewmodel/ProfileViewModel.kt
package com.example.perfulandia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.AvatarRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// El UiState ahora solo contiene la URI, que viene del repositorio.
data class ProfileUiState(
    val imageUri: Uri? = null
)

class ProfileViewModel(
    private val avatarRepository: AvatarRepository
) : ViewModel() {

    // El estado de la UI se deriva directamente del flujo del repositorio.
    val uiState: StateFlow<ProfileUiState> = avatarRepository.avatarUri
        .map { uri -> ProfileUiState(imageUri = uri) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState() // Estado inicial mientras se carga desde DataStore
        )

    /**
     * Llama al repositorio para guardar la nueva URI de la imagen.
     */
    fun onImageChange(uri: Uri?) {
        viewModelScope.launch {
            avatarRepository.saveAvatarUri(uri)
        }
    }
}

/**
 * Factory para poder inyectar el AvatarRepository en el ProfileViewModel.
 * Esto es necesario porque el ViewModel ahora tiene un constructor con parámetros.
 */
class ProfileViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(AvatarRepository(application)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
