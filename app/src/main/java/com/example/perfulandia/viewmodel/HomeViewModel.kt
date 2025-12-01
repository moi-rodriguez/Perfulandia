package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.CategoriaRepository
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.model.Categoria
import com.example.perfulandia.model.Perfume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la pantalla Home.
 * @property allPerfumes La lista completa original (sin filtrar).
 * @property filteredPerfumes La lista que se debe mostrar en pantalla (filtrada).
 * @property selectedCategoryId El ID de la categoría seleccionada (null = "Todos").
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val allPerfumes: List<Perfume> = emptyList(),
    val filteredPerfumes: List<Perfume> = emptyList(),
    val categories: List<Categoria> = emptyList(),
    val selectedCategoryId: String? = null,
    val error: String? = null
)

class HomeViewModel(
    private val perfumeRepository: PerfumeRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    /**
     * Carga inicial de datos (Categorías y Perfumes).
     */
    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // 1. Cargar Categorías
            val catResult = categoriaRepository.getAllCategorias()
            val categoriesList = catResult.getOrDefault(emptyList())

            // 2. Cargar Perfumes
            val perfResult = perfumeRepository.getAllPerfumes()

            perfResult.onSuccess { perfumes ->
                _uiState.value = HomeUiState(
                    isLoading = false,
                    allPerfumes = perfumes,
                    filteredPerfumes = perfumes, // Al inicio mostramos todos
                    categories = categoriesList,
                    selectedCategoryId = null
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar perfumes: ${e.message}"
                )
            }
        }
    }

    /**
     * Filtra la lista de perfumes por categoría.
     * @param categoryId El ID de la categoría a filtrar (o null para ver todos).
     */
    fun selectCategory(categoryId: String?) {
        val currentState = _uiState.value

        // Si ya estaba seleccionada, la deseleccionamos (toggle)
        val newSelectedId = if (currentState.selectedCategoryId == categoryId) null else categoryId

        val filteredList = if (newSelectedId == null) {
            currentState.allPerfumes // Mostrar todos
        } else {
            // Filtrar localmente (tu modelo Perfume no tiene campo categoriaId visible en models.kt,
            // pero asumiremos que el filtrado sería por backend o que agregaremos esa lógica después.
            // *NOTA PARA MVP:* Como el modelo `Perfume` actual no tiene `categoriaId`,
            // este filtro por ahora no hará nada visualmente exacto a menos que agregues ese campo.
            // Simulemos un filtro simple o dejémoslo listo para cuando actualices el modelo:
            currentState.allPerfumes
            // TODO: Descomentar cuando Perfume tenga categoriaId:
            // currentState.allPerfumes.filter { it.categoriaId == newSelectedId }
        }

        _uiState.value = currentState.copy(
            selectedCategoryId = newSelectedId,
            filteredPerfumes = filteredList
        )
    }
}