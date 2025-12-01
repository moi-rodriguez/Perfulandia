package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: AuthData? = null
)

data class AuthData(
    @SerializedName("user") val user: UserDto,
    @SerializedName("access_token") val token: String
)

/**
 * ProfileResponse: Envoltorio para la respuesta del perfil de usuario.
 */
data class ProfileResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    // Contiene los datos del usuario directamente.
    @SerializedName("data")
    val data: UserDto? = null
)

data class UsersResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<UserDto>,
    @SerializedName("message") val message: String? = null
)

data class ErrorResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: String? = null
)