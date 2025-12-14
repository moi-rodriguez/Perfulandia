package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.data.repository.ReviewRepository

/**
 * Fábrica para crear instancias de [PerfumeDetailViewModel].
 *
 * Es necesaria para poder inyectar las dependencias (PerfumeRepository, SessionManager, etc.)
 * en el constructor del ViewModel.
 */
class PerfumeDetailViewModelFactory(
    private val perfumeRepository: PerfumeRepository,
    private val reviewRepository: ReviewRepository, // Añadido
    private val sessionManager: SessionManager,
    private val cartViewModel: CartViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfumeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfumeDetailViewModel(
                perfumeRepository,
                reviewRepository, // Añadido
                sessionManager,
                cartViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}