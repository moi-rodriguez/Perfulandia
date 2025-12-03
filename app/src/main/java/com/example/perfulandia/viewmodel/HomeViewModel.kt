package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.PerfumeRepository
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
    val selectedGenero: String = "Todos",
    val error: String? = null
)

class HomeViewModel(
    private val perfumeRepository: PerfumeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPerfumes()
    }

    /**
     * Carga la lista de perfumes.
     */
    fun loadPerfumes() {

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = perfumeRepository.getAllPerfumes()

            result.onSuccess { listaReal ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        allPerfumes = listaReal,
                        perfumes = listaReal
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error de conexión: ${error.message}",
                        allPerfumes = emptyList(),
                        perfumes = emptyList()
                    )
                }
            }
        }
    }

    /**
     * Filtro de gènero
     */
    fun filterByGenero(genero: String) {
        _uiState.update { current ->
            val filteredList = if (genero == "Todos") {
                current.allPerfumes
            } else {
                current.allPerfumes.filter { it.genero == genero }
            }
            current.copy(selectedGenero = genero, perfumes = filteredList)
        }
    }
}