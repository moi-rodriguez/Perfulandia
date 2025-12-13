package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.OrderDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApi {
    @POST("pedido")
    suspend fun createOrder(@Body order: OrderDto): Response<Unit>
}
