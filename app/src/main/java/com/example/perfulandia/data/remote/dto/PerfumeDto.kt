package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * PerfumeDto: Representa un perfume recibio de la API
 */
data class PerfumeDto(
    @SerializedName("_id")
    val _id: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("marca")
    val marca: String? = null,

    @SerializedName("fragancia")
    val fragancia: String? = null,

    @SerializedName("tamaño")
    val tamano: Int? = null,

    @SerializedName("genero")
    val genero: String? = null,

    @SerializedName("precio")
    val precio: Double? = null,

    @SerializedName("stock")
    val stock: Int? = null,

    @SerializedName("categoria")
    val categoria: Any? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("imagen")
    val imagen: String? = null,

    @SerializedName("imagenThumbnail")
    val imagenThumbnail: String? = null
)

/**
 * PerfumesResponse: Respuesta genérica parar listas de perfumes
 */
data class PerfumesResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<PerfumeDto>,

    @SerializedName("message")
    val message: String? = null
)