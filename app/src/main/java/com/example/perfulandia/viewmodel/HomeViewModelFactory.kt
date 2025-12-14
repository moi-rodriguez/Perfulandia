package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.perfulandia.data.repository.CategoryRepository
import com.example.perfulandia.data.repository.PerfumeRepository

/**
 * [ViewModelProvider.Factory] para crear instancias de [HomeViewModel].
 *
 * Esta fábrica es necesaria porque [HomeViewModel] tiene dependencias (PerfumeRepository, CategoryRepository)
 * en su constructor. Sin una fábrica, el sistema Android no sabría cómo instanciar el ViewModel
 * con estas dependencias.
 *
 * Utilizar una fábrica asegura que el ViewModel se crea correctamente y su ciclo de vida
 * es gestionado por el sistema Android, sobreviviendo a los cambios de configuración
 * (ej. rotación de pantalla) y evitando la pérdida de estado o recargas innecesarias.
 */
class HomeViewModelFactory(
    private val perfumeRepository: PerfumeRepository,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(perfumeRepository, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}