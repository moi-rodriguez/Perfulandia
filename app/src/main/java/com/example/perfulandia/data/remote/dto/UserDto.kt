package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * UserDto: Representa los datos del usuario que vienen desde la API de Perfulandia
 *
 * Endpoints que usan este DTO:
 * - POST /auth/register → Respuesta con usuario creado
 * - GET /auth/profile → Respuesta con perfil del usuario
 * - GET /auth/users → Lista de usuarios (solo admin)
 */
data class UserDto(
    @SerializedName("_id")
    val _id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
