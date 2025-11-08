package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de autenticación (login o signup)
 * Datos que RECIBIMOS del servidor tras autenticación exitosa
 */
data class AuthResponse(
    @SerializedName("authToken")
    val authToken: String,  // Token de autenticación (guardar en SessionManager)

    @SerializedName("user_id")
    val userId: String
)
