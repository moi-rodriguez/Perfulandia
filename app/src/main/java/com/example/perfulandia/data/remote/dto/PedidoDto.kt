package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * PedidoDto: Representa un pedido
 *
 * Endpoints que usan este DTO:
 * - GET /pedido → Lista todos los pedidos
 * - GET /pedido/{id} → Obtiene un pedido por ID
 * - POST /pedido → Crea un nuevo pedido
 * - PATCH /pedido/{id} → Actualiza un pedido
 * - DELETE /pedido/{id} → Elimina un pedido
 * - POST /pedido/{id}/upload-image → Sube imagen a pedido
 */
data class PedidoDto(
    @SerializedName("_id")
    val _id: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("imagen")
    val imagen: String? = null,

    @SerializedName("imagenThumbnail")
    val imagenThumbnail: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

/**
 * PedidoResponse: Respuesta al obtener un pedido
 */
data class PedidoResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: PedidoDto,

    @SerializedName("message")
    val message: String? = null
)

/**
 * PedidosResponse: Respuesta al listar pedidos
 */
data class PedidosResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<PedidoDto>,

    @SerializedName("message")
    val message: String? = null
)

/**
 * CreatePedidoRequest: Para crear un nuevo pedido
 */
data class CreatePedidoRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null
)

/**
 * UpdatePedidoRequest: Para actualizar un pedido
 */
data class UpdatePedidoRequest(
    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null
)