package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName


/**
 * AuthResponse: Respuesta de autenticación (login o register)
 *
 * Endpoints que usan este DTO:
 * - POST /auth/login → Iniciar sesión
 * - POST /auth/register → Registrar nuevo usuario
 */
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    // leemos el objeto "data" en lugar de "user" y "token" directos
    @SerializedName("data")
    val data: AuthData? = null
)

// Clase auxiliar para leer el contenido de "data"
data class AuthData(
    @SerializedName("user")
    val user: UserDto,

    // CORRECCIÓN: El servidor lo llama "access_token", no "token"
    @SerializedName("access_token")
    val token: String
)

/**
 * ProfileResponse: Respuesta de GET /auth/profile
 */
data class ProfileResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("user")
    val user: UserDto,

    @SerializedName("message")
    val message: String? = null
)

/**
 * UsersResponse: Respuesta de GET /auth/users (lista de usuarios - solo admin)
 */
data class UsersResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<UserDto>,

    @SerializedName("message")
    val message: String? = null
)

// Respuesta de error
data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String,

    @SerializedName("error")
    val error: String? = null
)