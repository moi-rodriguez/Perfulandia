package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO = Data Transfer Object
 * Representa los datos del usuario que vienen desde el servidor
 */
data class UserDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("account_id")
    val accountId: Int,

    @SerializedName("role")
    val role: String
)
