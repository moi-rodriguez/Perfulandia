package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.ReviewDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReviewApi {
    @POST("resena")
    suspend fun createReview(@Body review: ReviewDto): Response<Unit>
}
