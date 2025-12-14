package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.OrderDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Wrapper para la respuesta al crear o pedir un solo pedido
 */
data class SingleOrderResponse(
    val success: Boolean,
    val data: OrderDto,
    val message: String?
)

/**
 * Wrapper para la respuesta de la lista de pedidos
 */
data class OrderListResponse(
    val success: Boolean,
    val data: List<OrderDto>,
    val message: String?
)


interface OrderApi {
    @POST("pedido")
    suspend fun createOrder(@Body order: OrderDto): Response<SingleOrderResponse>

    @GET("pedido")
    suspend fun getMyOrders(): Response<OrderListResponse>
}
