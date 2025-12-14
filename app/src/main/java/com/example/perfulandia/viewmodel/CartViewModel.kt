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

    /**
     * Añade un perfume al carrito. Si ya existe, incrementa su cantidad.
     */
    fun addToCart(perfume: Perfume) {
        val currentItems = _uiState.value.items.toMutableList()
        val existingItem = currentItems.find { it.perfume.id == perfume.id }

        if (existingItem != null) {
            incrementQuantity(perfume.id)
        } else {
            currentItems.add(CartItem(perfume, 1))
            updateCartState(currentItems)
        }
    }

    /**
     * Elimina un item del carrito por completo, sin importar la cantidad.
     */
    fun removeItem(perfumeId: String) {
        val currentItems = _uiState.value.items.toMutableList()
        currentItems.removeAll { it.perfume.id == perfumeId }
        updateCartState(currentItems)
    }

    /**
     * Incrementa la cantidad de un item en el carrito.
     */
    fun incrementQuantity(perfumeId: String) {
        val currentItems = _uiState.value.items.toMutableList()
        val existingItem = currentItems.find { it.perfume.id == perfumeId }

        if (existingItem != null) {
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
            updateCartState(currentItems)
        }
    }

    /**
     * Decrementa la cantidad de un item. Si la cantidad llega a cero, lo elimina.
     */
    fun decrementQuantity(perfumeId: String) {
        val currentItems = _uiState.value.items.toMutableList()
        val existingItem = currentItems.find { it.perfume.id == perfumeId }

        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                val index = currentItems.indexOf(existingItem)
                currentItems[index] = existingItem.copy(quantity = existingItem.quantity - 1)
                updateCartState(currentItems)
            } else {
                // Si la cantidad es 1, eliminar el item
                removeItem(perfumeId)
            }
        }
    }

    /**
     * Vacía el carrito por completo.
     */
    fun clearCart() {
        updateCartState(emptyList())
    }

    /**
     * Recalcula el total y actualiza el estado del carrito.
     */
    private fun updateCartState(items: List<CartItem>) {
        val total = items.sumOf { it.perfume.precio * it.quantity }
        _uiState.value = CartUiState(items = items, total = total)
    }
}