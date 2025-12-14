package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.CategoryRepository
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.model.Category
import com.example.perfulandia.model.Perfume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val perfumes: List<Perfume> = emptyList(),      // Lista filtrada que ve el usuario
    val allPerfumes: List<Perfume> = emptyList(),   // Copia de respaldo para resetear filtros
    val categories: List<Category> = emptyList(),   // Lista de categorías para filtros
    val selectedCategory: String = "Todos",
    val error: String? = null
)

class HomeViewModel(
    private val perfumeRepository: PerfumeRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * Carga tanto perfumes como categorías.
     */
    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Cargar perfumes
            val perfumeResult = perfumeRepository.getAllPerfumes()
            perfumeResult.onSuccess { perfumesList ->
                _uiState.update {
                    it.copy(
                        allPerfumes = perfumesList,
                        perfumes = perfumesList
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = "Error al cargar perfumes: ${error.message}",
                        allPerfumes = emptyList(),
                        perfumes = emptyList()
                    )
                }
            }

            // Cargar categorías
            val categoryResult = categoryRepository.getAllCategories()
            categoryResult.onSuccess { categoriesList ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = listOf(Category(id = "Todos", nombre = "Todos")) + categoriesList // Añadir "Todos" como opción
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar categorías: ${error.message}",
                        categories = listOf(Category(id = "Todos", nombre = "Todos"))
                    )
                }
            }
        }
    }

    /**
     * Filtro por categoría
     */
    fun filterByCategory(categoryName: String) {
        _uiState.update { current ->
            val filteredList = if (categoryName == "Todos") {
                current.allPerfumes
            } else {
                current.allPerfumes.filter { it.categoriaId == categoryName } // Asumiendo que categoriaId es el nombre o un ID único
            }
            current.copy(selectedCategory = categoryName, perfumes = filteredList)
        }
    }
}