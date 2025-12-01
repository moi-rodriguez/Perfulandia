package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * LoginRequest: Datos para iniciar sesión
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

/**
 * RegisterRequest: Datos para registrar un nuevo usuario
 */
data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    // // El rol debe ser exactamente "CLIENTE" para usuarios finales según requerimientos de la API.
    @SerializedName("role")
    val role: String = "CLIENTE",

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("preferencias")
    val preferencias: List<String>
)