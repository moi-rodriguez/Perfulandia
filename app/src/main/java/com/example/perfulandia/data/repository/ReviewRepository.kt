package com.example.perfulandia.data.repository

import com.example.perfulandia.data.remote.api.ReviewApi
import com.example.perfulandia.data.remote.dto.ReviewDto

class ReviewRepository(private val reviewApi: ReviewApi) {

    suspend fun createReview(perfumeId: String, clienteId: String, puntuacion: Int, comentario: String): Result<Unit> {
        return try {
            val reviewDto = ReviewDto(perfume = perfumeId, cliente = clienteId, puntuacion = puntuacion, comentario = comentario)
            val response = reviewApi.createReview(reviewDto)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al crear la reseña: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}
