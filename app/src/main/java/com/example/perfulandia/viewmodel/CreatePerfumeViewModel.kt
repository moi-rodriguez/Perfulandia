package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.model.Perfume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreatePerfumeUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class CreatePerfumeViewModel(private val perfumeRepository: PerfumeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePerfumeUiState())
    val uiState: StateFlow<CreatePerfumeUiState> = _uiState.asStateFlow()

    fun createPerfume(perfume: Perfume) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = perfumeRepository.createPerfume(perfume)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, success = true) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = throwable.message) }
            }
        }
    }
}

class CreatePerfumeViewModelFactory(private val perfumeRepository: PerfumeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatePerfumeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreatePerfumeViewModel(perfumeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}