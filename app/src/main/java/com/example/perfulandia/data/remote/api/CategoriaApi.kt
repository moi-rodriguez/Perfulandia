package com.example.perfulandia.data.remote.api


import com.example.perfulandia.data.remote.dto.CategoriaResponse
import com.example.perfulandia.data.remote.dto.CategoriasResponse
import com.example.perfulandia.data.remote.dto.CreateCategoriaRequest
import com.example.perfulandia.data.remote.dto.UpdateCategoriaRequest
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
 * CategoriaApi: Endpoints de categorías
 * Base URL: /categoria
 * Todos los endpoints requieren autenticación (Token JWT)
 */

interface CategoriaApi{
    /**
     * Obtener Categoria por ID
     * RUTA: GET /api/categoria/{id}
     */
    @GET("categoria/{id}")
    suspend fun getCategoriaById(
        @Path("id") id: String
    ): Response<CategoriaResponse>

    /**
     * DELETE /categoria/{id}
     * Eliminar un categoria
     *
     * @param id ID del categoria
     * @return Confirmación de eliminación
     */

    @DELETE("categoria/{id}")
    suspend fun deleteCategoria(
        @Path("id") id: String
    ): Response<CategoriaResponse>



    /**
     * GET /categoria
     * Listar todas las categorías
     *
     * @return Lista de categorías
     */

    @GET("categoria")
    suspend fun getAllCategorias(): Response<CategoriasResponse>

    /**
     * POST /categoría
     *
     * @param request Datos de la categoría (nombre, descripcion)
     * @return Categoría creada
     */
    @POST("categoria")
    suspend fun createCategoria(
        @Body request: CreateCategoriaRequest
    ): Response<CategoriaResponse>

    /**
     * PATCH /categoria/{id}
     * Actualizar una categoría
     * @param id ID de la categoría
     * @param request Datos a actualizar
     * @return Categoría actualizada
     */
    @PATCH("categoria/{id}")
    suspend fun updateCategoria(
        @Path("id") id: String,
        @Body request: UpdateCategoriaRequest
    ): Response<CategoriaResponse>

    /**
     * POST /categoria/{id}/upload-image
     * Subir imagen a una categoría
     *
     * @param id ID de la categoría
     * @param image Imagen a subir (multipart/form-data)
     * @return Categoría con la imagen actualizada
     */

    @Multipart
    @POST("categoria/{id}/upload-image")
    suspend fun uploadImage(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<CategoriaResponse>
}