package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * ClienteDto: Representa un cliente
 *
 * Endpoints que usan este DTO:
 * - GET /cliente → Lista todos los clientes
 * - GET /cliente/{id} → Obtiene un cliente por ID
 * - POST /cliente → Crea un nuevo cliente
 * - PATCH /cliente/{id} → Actualiza un cliente
 * - DELETE /cliente/{id} → Elimina un cliente
 * - POST /cliente/{id}/upload-image → Sube imagen a cliente
 */
data class ClienteDto(
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
 * ClienteResponse: Respuesta al obtener un cliente
 */
data class ClienteResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: ClienteDto,

    @SerializedName("message")
    val message: String? = null
)

/**
 * ClientesResponse: Respuesta al listar clientes
 */
data class ClientesResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<ClienteDto>,

    @SerializedName("message")
    val message: String? = null
)

/**
 * CreateClienteRequest: Para crear un nuevo cliente
 */
data class CreateClienteRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null
)

/**
 * UpdateClienteRequest: Para actualizar un cliente
 */
data class UpdateClienteRequest(
    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null
)