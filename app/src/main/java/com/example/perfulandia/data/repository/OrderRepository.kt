package com.example.perfulandia.data.repository

import com.example.perfulandia.data.mapper.OrderMapper
import com.example.perfulandia.data.remote.api.OrderApi
import com.example.perfulandia.data.remote.dto.OrderDto
import com.example.perfulandia.data.remote.dto.OrderItemDto
import com.example.perfulandia.model.Order

class OrderRepository(private val orderApi: OrderApi) {

    /**
     * Obtiene el historial de pedidos del usuario logueado
     */
    suspend fun getMyOrders(): Result<List<Order>> {
        return try {
            val response = orderApi.getMyOrders()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val domainOrders = OrderMapper.fromDtoList(body.data)
                    Result.success(domainOrders)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener la lista de pedidos"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Envía un nuevo pedido al backend
     */
    suspend fun checkout(clientId: String, items: List<OrderItemDto>, total: Double): Result<Unit> {
        return try {
            val orderDto = OrderDto(cliente = clientId, items = items, total = total)
            val response = orderApi.createOrder(orderDto)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Error al crear el pedido: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}
