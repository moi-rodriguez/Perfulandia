package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * PerfumeDto: Representa un perfume
 *
 * Endpoints que usan este DTO:
 * - GET /perfume → Lista todos los perfumes
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
    val updatedAt: String? = null,

    @SerializedName("marca")
    val marca: String? = null,

    @SerializedName("fragancia")
    val fragancia: String? = null,

    @SerializedName("genero")
    val genero: String? = null,

    @SerializedName("precio")
    val precio: Double? = null,

    @SerializedName("stock")
    val stock: Int? = null,
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