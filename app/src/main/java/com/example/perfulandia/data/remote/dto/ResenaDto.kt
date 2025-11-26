package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * ResenaDto: Representa una reseña
 *
 * Endpoints que usan este DTO:
 * - GET /resena → Lista todas las reseñas
 * - GET /resena/{id} → Obtiene una reseña por ID
 * - POST /resena → Crea una nueva reseña
 * - PATCH /resena/{id} → Actualiza una reseña
 * - DELETE /resena/{id} → Elimina una reseña
 * - POST /resena/{id}/upload-image → Sube imagen a reseña
 */

data class ResenaDto(
    @SerializedName("_id")
    val _id: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("imagenThumbnail")
    val imagen: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

/**
 * ResenaResponse: Respuesta al obtener una reseña
 */
data class ResenaResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: ResenaDto,

    @SerializedName("message")
    val message: String? = null
)

/**
 * ResenasResponse: Respuesta al listar reseñas
 */
data class ResenasResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<ResenaDto>,

    @SerializedName("message")
    val message: String? = null
)

/**
 * CreateResenaRequest: Para crear una nueva reseña
 */
data class CreateResenaRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null
)
/**
 * UpdateResenaRequest: Para actualizar una reseña
 */

data class UpdateResenaRequest(
    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null
)




