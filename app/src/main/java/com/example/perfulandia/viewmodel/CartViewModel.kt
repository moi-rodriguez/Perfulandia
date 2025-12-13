package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import com.example.perfulandia.model.Perfume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CartItem(val perfume: Perfume, val quantity: Int)

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)

class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addToCart(perfume: Perfume) {
        val currentItems = _uiState.value.items.toMutableList()
        val existingItem = currentItems.find { it.perfume.id == perfume.id }

        if (existingItem != null) {
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentItems.add(CartItem(perfume, 1))
        }

        updateCartState(currentItems)
    }

    fun removeFromCart(perfume: Perfume) {
        val currentItems = _uiState.value.items.toMutableList()
        val existingItem = currentItems.find { it.perfume.id == perfume.id }

        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                val index = currentItems.indexOf(existingItem)
                currentItems[index] = existingItem.copy(quantity = existingItem.quantity - 1)
            } else {
                currentItems.remove(existingItem)
            }
        }

        updateCartState(currentItems)
    }

    fun clearCart() {
        updateCartState(emptyList())
    }

    private fun updateCartState(items: List<CartItem>) {
        val total = items.sumOf { it.perfume.precio * it.quantity }
        _uiState.value = CartUiState(items = items, total = total)
    }
}