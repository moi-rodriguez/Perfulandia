package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.PerfumesResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * PerfumeApi: Endpoints de perfumes
 * Todos los endpoints requieren autenticación (Token JWT)
 */
interface PerfumeApi {

    /**
     * GET /perfume
     * Obtener lista de perfumes
     */
    @GET("perfume")
    suspend fun getAllPerfumes(): Response<PerfumesResponse>
}