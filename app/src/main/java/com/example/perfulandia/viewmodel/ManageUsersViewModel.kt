package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.UserRepository
import com.example.perfulandia.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ManageUsersUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)

class ManageUsersViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageUsersUiState())
    val uiState: StateFlow<ManageUsersUiState> = _uiState.asStateFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = userRepository.getUsers()

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    users = it
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message ?: "Error desconocido"
                )
            }
        }
    }
}