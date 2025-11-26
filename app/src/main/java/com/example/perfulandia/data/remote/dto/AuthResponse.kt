package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * AuthResponse: Respuesta de autenticación (login o register)
 *
 * Endpoints que usan este DTO:
 * - POST /auth/login → Iniciar sesión
 * - POST /auth/register → Registrar nuevo usuario
 *
 * Ejemplo de respuesta JSON de tu API:
 * {
 *   "success": true,
 *   "message": "Login exitoso",
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NzRhY...",
 *   "user": {
 *     "_id": "674ac123456789abcdef",
 *     "nombre": "Administrador",
 *     "email": "admin@sistema.com",
 *     "role": "admin",
 *     "createdAt": "2024-11-25T10:30:00.000Z"
 *   }
 * }
 */
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("token")
    val token: String,  // Token JWT para autenticación

    @SerializedName("user")
    val user: UserDto,  // Datos completos del usuario

    @SerializedName("message")
    val message: String? = null  // Mensaje opcional (ej: "Login exitoso")
)

/**
 * ProfileResponse: Respuesta de GET /auth/profile
 *
 * Ejemplo de respuesta JSON:
 * {
 *   "success": true,
 *   "user": {
 *     "_id": "674ac123456789abcdef",
 *     "nombre": "Administrador",
 *     "email": "admin@sistema.com",
 *     "role": "admin"
 *   }
 * }
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
 *
 * Ejemplo de respuesta JSON:
 * {
 *   "success": true,
 *   "data": [
 *     {
 *       "_id": "674ac123456789abcdef",
 *       "nombre": "Administrador",
 *       "email": "admin@sistema.com",
 *       "role": "admin"
 *     },
 *     {
 *       "_id": "674ac987654321fedcba",
 *       "nombre": "Usuario Normal",
 *       "email": "usuario@sistema.com",
 *       "role": "user"
 *     }
 *   ]
 * }
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