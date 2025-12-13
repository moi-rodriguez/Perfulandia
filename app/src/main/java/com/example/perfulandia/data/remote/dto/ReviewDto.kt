package com.example.perfulandia.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("perfume")
    val perfume: String,

    @SerializedName("cliente")
    val cliente: String,

    @SerializedName("puntuacion")
    val puntuacion: Int,

    @SerializedName("comentario")
    val comentario: String
)
