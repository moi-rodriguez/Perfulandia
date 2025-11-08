package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la petición de registro (signup)
 * Datos que ENVIAMOS al servidor
 */
data class SignupRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
