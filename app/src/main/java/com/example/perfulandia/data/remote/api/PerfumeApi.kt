package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.PerfumeDto
import com.example.perfulandia.data.remote.dto.PerfumesResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.QueryMap

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

    /**
     * Obtener detalle de uno solo por ID
     */
    @GET("perfume/{id}")
    suspend fun getPerfumeById(
        @Path("id") id: String
    ): Response<SinglePerfumeResponse>

    /**
     * GET /perfume/categoria/{categoryId}
     * Obtener perfumes por categoría
     */
    @GET("perfume/categoria/{categoryId}")
    suspend fun getPerfumesByCategory(
        @Path("categoryId") categoryId: String
    ): Response<PerfumesResponse>

    // Filtrar perfumes
    @GET("perfume/filtros")
    suspend fun filterPerfumes(
        @QueryMap filters: Map<String, String>
    ): Response<PerfumesResponse>

    /**
     * POST /perfume
     * Crear un nuevo perfume
     */
    @POST("perfume")
    suspend fun createPerfume(
        @Body perfume: PerfumeDto
    ): Response<SinglePerfumeResponse>

    /**
     * PATCH /perfume/{id}
     * Actualizar un perfume existente
     */
    @PATCH("perfume/{id}")
    suspend fun updatePerfume(
        @Path("id") id: String,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<SinglePerfumeResponse>

    /**
     * DELETE /perfume/{id}
     * Eliminar un perfume
     */
    @DELETE("perfume/{id}")
    suspend fun deletePerfume(
        @Path("id") id: String
    ): Response<Unit> // O un tipo de respuesta más específico si la API lo devuelve

    /**
     * POST /perfume/{id}/upload-image
     * Subir una imagen para un perfume
     */
    @Multipart
    @POST("perfume/{id}/upload-image")
    suspend fun uploadPerfumeImage(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<SinglePerfumeResponse>
}

/**
 * Wrapper simple para cuando la API devuelve un solo perfume en 'data'
 */
data class SinglePerfumeResponse(
    val success: Boolean,
    val data: PerfumeDto?,
    val message: String?
)