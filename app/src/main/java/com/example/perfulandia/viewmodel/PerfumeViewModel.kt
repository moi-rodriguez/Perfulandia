package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Importamos tu Repositorio y el Modelo que me pasaste
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.model.Perfume

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Estado de la UI para la sección de Perfumes.
 * @param perfumes Lista de perfumes para mostrar en el RecyclerView/LazyColumn.
 * @param selectedPerfume El perfume seleccionado para ver en detalle.
 */
data class PerfumeUiState(
    val isLoading: Boolean = false,
    val perfumes: List<Perfume> = emptyList(),
    val selectedPerfume: Perfume? = null,
    val error: String? = null
)

/**
 * ViewModel para gestionar perfumes.
 * Inyecta PerfumeRepository en el constructor.
 */
class PerfumeViewModel(
    private val perfumeRepository: PerfumeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfumeUiState())
    val uiState: StateFlow<PerfumeUiState> = _uiState.asStateFlow()

    init {
        // Cargamos la lista automáticamente al iniciar el ViewModel
        loadPerfumes()
    }

    /**
     * Obtiene la lista de todos los perfumes.
     */
    fun loadPerfumes() {
        // Activamos carga y limpiamos errores, pero MANTENEMOS la lista actual
        // para que no parpadee la pantalla si ya había datos.
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = perfumeRepository.getAllPerfumes()

            _uiState.update { current ->
                result.fold(
                    onSuccess = { perfumeList ->
                        current.copy(
                            isLoading = false,
                            perfumes = perfumeList,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        val friendlyMessage = handleException(exception)
                        current.copy(
                            isLoading = false,
                            error = friendlyMessage
                            // No borramos la lista 'perfumes' en caso de error para que el usuario siga viendo lo que había
                        )
                    }
                )
            }
        }
    }

    /**
     * Obtiene el detalle de un perfume por su ID.
     */
    fun loadPerfumeDetail(id: String) {
        _uiState.update { it.copy(isLoading = true, error = null, selectedPerfume = null) }

        viewModelScope.launch {
            val result = perfumeRepository.getPerfumeById(id)

            _uiState.update { current ->
                result.fold(
                    onSuccess = { perfume ->
                        current.copy(
                            isLoading = false,
                            selectedPerfume = perfume,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        val friendlyMessage = handleException(exception)
                        current.copy(
                            isLoading = false,
                            error = friendlyMessage,
                            selectedPerfume = null
                        )
                    }
                )
            }
        }
    }

    /**
     * Limpia la selección (útil al volver de la pantalla de detalles a la lista)
     */
    fun clearSelection() {
        _uiState.update { it.copy(selectedPerfume = null) }
    }

    /**
     * Manejador centralizado de errores
     */
    private fun handleException(exception: Throwable): String {
        return when (exception) {
            is HttpException -> {
                when (exception.code()) {
                    401 -> "Sesión expirada. Por favor, inicia sesión de nuevo."
                    403 -> "No tienes permisos para ver estos perfumes."
                    404 -> "No se encontraron los datos solicitados."
                    500 -> "Error del servidor. Intenta más tarde."
                    else -> "Error de conexión (${exception.code()})."
                }
            }
            is UnknownHostException -> "Sin conexión a internet."
            is SocketTimeoutException -> "El servidor tardó mucho en responder."
            else -> exception.localizedMessage ?: "Error desconocido."
        }
    }
}