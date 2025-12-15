package com.example.perfulandia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfulandia.data.repository.OrderRepository
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.dto.OrderItemDto
import com.example.perfulandia.model.Perfume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartItem(val perfume: Perfume, val quantity: Int)

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)

// Estado para el proceso de realizar un pedido
sealed class OrderPlacementState {
    object Idle : OrderPlacementState()
    object Loading : OrderPlacementState()
    object Success : OrderPlacementState()
    data class Error(val message: String) : OrderPlacementState()
}

class CartViewModel(
    private val orderRepository: OrderRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _orderPlacementState = MutableStateFlow<OrderPlacementState>(OrderPlacementState.Idle)
    val orderPlacementState: StateFlow<OrderPlacementState> = _orderPlacementState.asStateFlow()

    /**
     * Procesa y envía el pedido al backend.
     */
    fun placeOrder() {
        viewModelScope.launch {
            _orderPlacementState.value = OrderPlacementState.Loading
            val currentItems = _uiState.value.items
            if (currentItems.isEmpty()) {
                _orderPlacementState.value = OrderPlacementState.Error("El carrito está vacío.")
                return@launch
            }

            val clientId = sessionManager.getUserId()
            if (clientId == null) {
                _orderPlacementState.value = OrderPlacementState.Error("ID de cliente no disponible. Por favor, inicia sesión.")
                _orderPlacementState.value = OrderPlacementState.Idle // Reset state without success
                return@launch
            }

            val orderItemsDto = currentItems.map { cartItem ->
                OrderItemDto(
                    perfume = cartItem.perfume.id,
                    cantidad = cartItem.quantity,
                    precio = cartItem.perfume.precio
                )
            }
            val total = _uiState.value.total

            val result = orderRepository.checkout(clientId, orderItemsDto, total)

            result.fold(
                onSuccess = {
                    _orderPlacementState.value = OrderPlacementState.Success
                    clearCart() // Limpiar el carrito después de un pedido exitoso
                },
                onFailure = { exception ->
                    _orderPlacementState.value = OrderPlacementState.Error(exception.message ?: "Error al procesar el pedido")
                }
            )
        }
    }
    /**
     * Resetea el estado del pedido a Idle. Debe llamarse después de que la UI haya manejado
     * el estado de Success o Error (p. ej. después de navegar o mostrar un snackbar).
     */
    fun resetOrderPlacementState() {
        _orderPlacementState.value = OrderPlacementState.Idle
    }
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