package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * LoginRequest: Datos para iniciar sesión
 * Endpoint: POST /auth/login
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

/**
 * RegisterRequest: Datos para registrar un nuevo usuario
 * Endpoint: POST /auth/register
 */
data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("role")
    val role: String = "user"
)