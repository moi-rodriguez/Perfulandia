package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * PerfumeDto: Representa un perfume
 *
 * Endpoints que usan este DTO:
 * - GET /perfume → Lista todos los perfumes
 * - GET /perfume/{id} → Obtiene un perfume por ID
 * - POST /perfume → Crea un nuevo perfume
 * - PATCH /perfume/{id} → Actualiza un perfume
 * - DELETE /perfume/{id} → Elimina un perfume
 * - POST /perfume/{id}/upload-image → Sube imagen a perfume
 */
data class PerfumeDto(
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
 * PerfumeResponse: Respuesta al obtener un perfume
 */
data class PerfumeResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: PerfumeDto,

    @SerializedName("message")
    val message: String? = null
)

/**
 * PerfumesResponse: Respuesta al listar perfumes
 */
data class PerfumesResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<PerfumeDto>,

    @SerializedName("message")
    val message: String? = null
)

/**
 * CreatePerfumeRequest: Para crear un nuevo perfume
 */
data class CreatePerfumeRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null
)

/**
 * UpdatePerfumeRequest: Para actualizar un perfume
 */
data class UpdatePerfumeRequest(
    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null
)