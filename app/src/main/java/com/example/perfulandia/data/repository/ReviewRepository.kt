package com.example.perfulandia.data.repository

import com.example.perfulandia.data.mapper.ReviewMapper
import com.example.perfulandia.data.remote.api.ReviewApi
import com.example.perfulandia.data.remote.dto.ReviewDto
import com.example.perfulandia.model.Review

class ReviewRepository(private val reviewApi: ReviewApi) {

    /**
     * Obtiene las reseñas de un perfume específico
     */
    suspend fun getReviewsByPerfumeId(perfumeId: String): Result<List<Review>> {
        return try {
            val response = reviewApi.getReviewsByPerfumeId(perfumeId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val domainReviews = ReviewMapper.fromDtoList(body.data)
                    Result.success(domainReviews)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener las reseñas"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Crea una nueva reseña
     */
    suspend fun createReview(perfumeId: String, clienteId: String, puntuacion: Int, comentario: String): Result<Unit> {
        return try {
            val reviewDto = ReviewDto(perfume = perfumeId, cliente = clienteId, puntuacion = puntuacion, comentario = comentario)
            val response = reviewApi.createReview(reviewDto)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Error al crear la reseña: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}
