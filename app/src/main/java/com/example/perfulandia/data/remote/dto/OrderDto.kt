package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("cliente")
    val cliente: String, // Añadido el ID del cliente
    @SerializedName("items")
    val items: List<OrderItemDto>,
    @SerializedName("total")
    val total: Double
)

data class OrderItemDto(
    @SerializedName("perfume")
    val perfume: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio")
    val precio: Double
)
