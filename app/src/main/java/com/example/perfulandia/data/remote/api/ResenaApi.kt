package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.ResenaResponse
import com.example.perfulandia.data.remote.dto.ResenasResponse
import com.example.perfulandia.data.remote.dto.CreateResenaRequest
import com.example.perfulandia.data.remote.dto.UpdateResenaRequest
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

/**
 * ResenaApi: Endpoints de reseñas
 * Base URL: /resena
 * Todos los endpoints requieren autenticación (Token JWT)
 */
interface ResenaApi {

    /**
     * GET /resena
     * Listar todas las reseñas
     *
     * @return Lista de reseñas
     */
    @GET("resena")
    suspend fun getAllResenas(): Response<ResenasResponse>

    /**
     * GET /resena/{id}
     * Obtener una reseña por ID
     *
     * @param id ID de la reseña
     * @return Datos de la reseña
     */
    @GET("resena/{id}")
    suspend fun getResenaById(
        @Path("id") id: String
    ): Response<ResenaResponse>

    /**
     * POST /resena
     * Crear una nueva reseña
     *
     * @param request Datos de la reseña (nombre, descripcion)
     * @return Reseña creada
     */
    @POST("resena")
    suspend fun createResena(
        @Body request: CreateResenaRequest
    ): Response<ResenaResponse>

    /**
     * PATCH /resena/{id}
     * Actualizar una reseña
     *
     * @param id ID de la reseña
     * @param request Datos a actualizar
     * @return Reseña actualizada
     */
    @PATCH("resena/{id}")
    suspend fun updateResena(
        @Path("id") id: String,
        @Body request: UpdateResenaRequest
    ): Response<ResenaResponse>

    /**
     * DELETE /resena/{id}
     * Eliminar una reseña
     *
     * @param id ID de la reseña
     * @return Confirmación de eliminación
     */
    @DELETE("resena/{id}")
    suspend fun deleteResena(
        @Path("id") id: String
    ): Response<ResenaResponse>

    /**
     * POST /resena/{id}/upload-image
     * Subir imagen a una reseña
     *
     * @param id ID de la reseña
     * @param image Archivo de imagen (multipart/form-data)
     * @return Reseña con imagen actualizada
     */
    @Multipart
    @POST("resena/{id}/upload-image")
    suspend fun uploadImage(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<ResenaResponse>
}