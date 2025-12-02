package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.PerfumeResponse
import com.example.perfulandia.data.remote.dto.CreatePerfumeRequest
import com.example.perfulandia.data.remote.dto.UpdatePerfumeRequest
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
 * PerfumeApi: Endpoints de perfumes
 * Base URL: /perfume
 * Todos los endpoints requieren autenticación (Token JWT)
 */
interface PerfumeApi {

    @GET("perfume/{id}")
    suspend fun getPerfumeById(
        @Path("id") id: String
    ): Response<PerfumeResponse>

    /**
     * POST /perfume
     * Crear un nuevo perfume
     *
     * @param request Datos del perfume (nombre, descripcion)
     * @return Perfume creado
     */
    @POST("perfume")
    suspend fun createPerfume(
        @Body request: CreatePerfumeRequest
    ): Response<PerfumeResponse>

    /**
     * PATCH /perfume/{id}
     * Actualizar un perfume
     *
     * @param id ID del perfume
     * @param request Datos a actualizar
     * @return Perfume actualizado
     */
    @PATCH("perfume/{id}")
    suspend fun updatePerfume(
        @Path("id") id: String,
        @Body request: UpdatePerfumeRequest
    ): Response<PerfumeResponse>

    /**
     * DELETE /perfume/{id}
     * Eliminar un perfume
     *
     * @param id ID del perfume
     * @return Confirmación de eliminación
     */
    @DELETE("perfume/{id}")
    suspend fun deletePerfume(
        @Path("id") id: String
    ): Response<PerfumeResponse>

    /**
     * POST /perfume/{id}/upload-image
     * Subir imagen a un perfume
     *
     * @param id ID del perfume
     * @param image Archivo de imagen (multipart/form-data)
     * @return Perfume con imagen actualizada
     */
    @Multipart
    @POST("perfume/{id}/upload-image")
    suspend fun uploadImage(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<PerfumeResponse>
}