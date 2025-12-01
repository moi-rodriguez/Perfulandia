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

    // Datos de prueba para desarrollo y previsualización (Mock Data).
    private val mockPerfumes = listOf(
        Perfume(id = "1", nombre = "Chanel N°5", genero = "Femenino", descripcion = "El perfume de la eternidad.", imagen = "https://media.falabella.com/falabellaCL/270202_1/w=1500,h=1500,fit=cover"),
        Perfume(id = "2", nombre = "Dior Sauvage", genero = "Masculino", descripcion = "Una frescura radical y noble.", imagen = "https://cdnx.jumpseller.com/sairam/image/5929373/thumb/1500/1500?1654111131"),
        Perfume(id = "3", nombre = "Creed Aventus", genero = "Masculino", descripcion = "Fuerza, poder y éxito.", imagen = "https://cl-cenco-pim-resizer.ecomm.cencosud.com/unsafe/adaptive-fit-in/3840x0/filters:quality(75)/prd-cl/product-medias/6c2b0238-d04c-47c4-afb4-fb02ecc0773d/MKNFQHO2Y6/MKNFQHO2Y6-1/1758642147254-MKNFQHO2Y6-1-0.jpg"),
        Perfume(id = "4", nombre = "Light Blue D&G", genero = "Femenino", descripcion = "La quintaesencia de la alegría de vivir.", imagen = "https://media.falabella.com/falabellaCL/4750703_1/w=1500,h=1500,fit=cover"),
        Perfume(id = "5", nombre = "Black Opium YSL", genero = "Femenino", descripcion = "Un chute de adrenalina.", imagen = "https://cdnx.jumpseller.com/sairam/image/43732462/thumb/1500/1500?1703002665"),
        Perfume(id = "6", nombre = "CK One", genero = "Unisex", descripcion = "Un perfume para todos.", imagen = "https://static.beautytocare.com/media/catalog/product/c/a/calvin-klein-ck-one-eau-de-toilette-200ml_1.jpg"),
        Perfume(id = "7", nombre = "Paco Rabanne 1 Million", genero = "Masculino", descripcion = "El aroma del éxito.", imagen = "https://cdnx.jumpseller.com/sairam/image/9047411/thumb/1500/1500?1634971972")
    )

    init {
        loadPerfumes()
    }

    fun loadPerfumes() {
        /**
         * Carga la lista de perfumes.
         * Actualmente utiliza datos locales (Mock) para pruebas.
         * TODO: Descomentar llamada al repositorio al conectar con API de producción.
         */

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // SIMULAMOS CARGA (opcional, para ver el loading un momento)
            // kotlinx.coroutines.delay(500)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    allPerfumes = mockPerfumes,
                    perfumes = mockPerfumes // Al inicio mostramos todos
                )
            }
        }
    }

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