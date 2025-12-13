package com.example.perfulandia.data.repository

import com.example.perfulandia.data.remote.api.OrderApi
import com.example.perfulandia.data.remote.dto.OrderDto
import com.example.perfulandia.data.remote.dto.OrderItemDto

class OrderRepository(private val orderApi: OrderApi) {

    suspend fun checkout(items: List<OrderItemDto>, total: Double): Result<Unit> {
        return try {
            val orderDto = OrderDto(items = items, total = total)
            val response = orderApi.createOrder(orderDto)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al crear el pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}
