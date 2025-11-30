package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Importamos el Repositorio de Categorías y el Modelo de Dominio
import com.example.perfulandia.data.repository.CategoriaRepository
import com.example.perfulandia.model.Categoria

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Estado de la UI para la sección de Categorías.
 * @param isLoading Indica si se están cargando datos.
 * @param categorias Lista de todas las categorías disponibles.
 * @param selectedCategoria La categoría seleccionada (para ver detalles o filtrar productos).
 * @param error Mensaje de error si algo falla.
 */
data class CategoriaUiState(
    val isLoading: Boolean = false,
    val categorias: List<Categoria> = emptyList(),
    val selectedCategoria: Categoria? = null,
    val error: String? = null
)

/**
 * ViewModel para gestionar categorías.
 * Inyecta CategoriaRepository en el constructor.
 */
class CategoriaViewModel(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaUiState())
    val uiState: StateFlow<CategoriaUiState> = _uiState.asStateFlow()

    init {
        // Cargar las categorías automáticamente al iniciar
        loadCategorias()
    }

    /**
     * Obtiene la lista de todas las categorías desde el repositorio.
     */
    fun loadCategorias() {
        // Activamos loading pero mantenemos los datos previos para evitar parpadeos
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = categoriaRepository.getAllCategorias()

            _uiState.update { current ->
                result.fold(
                    onSuccess = { categoriaList ->
                        current.copy(
                            isLoading = false,
                            categorias = categoriaList,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        val friendlyMessage = handleException(exception)
                        current.copy(
                            isLoading = false,
                            error = friendlyMessage
                            // Mantenemos la lista anterior en caso de error de conexión temporal
                        )
                    }
                )
            }
        }
    }

    /**
     * Obtiene los detalles de una categoría específica por su ID.
     */
    fun loadCategoriaDetail(id: String) {
        _uiState.update { it.copy(isLoading = true, error = null, selectedCategoria = null) }

        viewModelScope.launch {
            val result = categoriaRepository.getCategoriaById(id)

            _uiState.update { current ->
                result.fold(
                    onSuccess = { categoria ->
                        current.copy(
                            isLoading = false,
                            selectedCategoria = categoria,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        val friendlyMessage = handleException(exception)
                        current.copy(
                            isLoading = false,
                            error = friendlyMessage,
                            selectedCategoria = null
                        )
                    }
                )
            }
        }
    }

    /**
     * Selecciona una categoría manualmente (por ejemplo, al hacer clic en una lista).
     * Útil si ya tienes el objeto categoría y no quieres hacer otra llamada a la API.
     */
    fun selectCategoria(categoria: Categoria) {
        _uiState.update { it.copy(selectedCategoria = categoria) }
    }

    /**
     * Limpia la selección actual.
     */
    fun clearSelection() {
        _uiState.update { it.copy(selectedCategoria = null) }
    }

    /**
     * Manejador centralizado de errores.
     */
    private fun handleException(exception: Throwable): String {
        return when (exception) {
            is HttpException -> {
                when (exception.code()) {
                    401 -> "Sesión expirada. Por favor, inicia sesión de nuevo."
                    403 -> "No tienes acceso a estas categorías."
                    404 -> "No se encontraron categorías."
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