package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * UserDto: Representa los datos del usuario que vienen desde la API
 *
 * Endpoints que usan este DTO:
 * - POST /auth/register → Respuesta con usuario creado
 * - POST /auth/login -> Respuesta con usuario logeado
 * - GET /auth/profile → Respuesta con perfil del usuario
 */
data class UserDto(
    @SerializedName("_id")
    val _id: String,

    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("email")
    val email: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("telefono")
    val telefono: String? = null,

    @SerializedName("direccion")
    val direccion: String? = null,

    @SerializedName("preferencias")
    val preferencias: List<String>? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
