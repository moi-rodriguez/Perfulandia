package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.ReviewDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Wrapper para la respuesta de una sola reseña
 */
data class SingleReviewResponse(
    val success: Boolean,
    val data: ReviewDto,
    val message: String?
)

/**
 * Wrapper para la respuesta de la lista de reseñas
 */
data class ReviewListResponse(
    val success: Boolean,
    val data: List<ReviewDto>,
    val message: String?
)

interface ReviewApi {
    @POST("resena")
    suspend fun createReview(@Body review: ReviewDto): Response<SingleReviewResponse>

    @GET("resena/perfume/{id}")
    suspend fun getReviewsByPerfumeId(@Path("id") perfumeId: String): Response<ReviewListResponse>
}
