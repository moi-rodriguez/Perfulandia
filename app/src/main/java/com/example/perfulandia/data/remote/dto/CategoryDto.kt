package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/*
Este modelo servirá para interpretar la respuesta
de las categorias que usaremos en los filtros
 */

/**
 * DTO para las categorias de perfumes (citiricos, amaderados, etc...)
 */
data class CategoryDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("tipo")
    val tipo: String? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("imagen")
    val imagen: String? = null
)

/**
 * Respuesta la listar categorias
 */
data class CategoriesResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<CategoryDto>,

    @SerializedName("message")
    val message: String? = null
)