package com.example.perfulandia.data.remote.dto
import com.google.gson.annotations.SerializedName

/**
 * CategoriaDto: Representa una categoría de perfumes
 *
 * Endpoints que usan este DTO:
 * - GET /categoria → Lista todas las categorías
 * - GET /categoria/{id} → Obtiene una categoría por ID
 * - POST /categoria → Crea una nueva categoría
 * - PATCH /categoria/{id} → Actualiza una categoría
 * - DELETE /categoria/{id} → Elimina una categoría
 * - POST /categoria/{id}/upload-image → Sube imagen a categoría
 */
data class CategoriaDto(
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
 * CategoriaResponse: Respuesta al obtener una categoría
 */
data class CategoriaResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: CategoriaDto,

    @SerializedName("message")
    val message: String? = null
)

/**
 * CategoriasResponse: Respuesta al listar categorías
 */
data class CategoriasResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<CategoriaDto>,

    @SerializedName("message")
    val message: String? = null
)

/**
 * CreateCategoriaRequest: Para crear una nueva categoría
 */
data class CreateCategoriaRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null
)

/**
 * UpdateCategoriaRequest: Para actualizar una categoría
 */
data class UpdateCategoriaRequest(
    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null
)