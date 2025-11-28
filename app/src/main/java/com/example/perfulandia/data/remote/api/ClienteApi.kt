package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.ClienteResponse
import com.example.perfulandia.data.remote.dto.ClientesResponse
import com.example.perfulandia.data.remote.dto.CreateClienteRequest
import com.example.perfulandia.data.remote.dto.UpdateClienteRequest
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
 * ClienteApi: Endpoints de clientes
 * Base URL: /cliente
 * Todos los endpoints requieren autenticación (Token JWT)
 */
interface ClienteApi {

    /**
     * GET /cliente
     * Listar todos los clientes
     *
     * @return Lista de clientes
     */
    @GET("cliente")
    suspend fun getAllClientes(): Response<ClientesResponse>

    /**
     * GET /cliente/{id}
     * Obtener un cliente por ID
     *
     * @param id ID del cliente
     * @return Datos del cliente
     */
    @GET("cliente/{id}")
    suspend fun getClienteById(
        @Path("id") id: String
    ): Response<ClienteResponse>

    /**
     * POST /cliente
     * Crear un nuevo cliente
     *
     * @param request Datos del cliente (nombre, descripcion)
     * @return Cliente creado
     */
    @POST("cliente")
    suspend fun createCliente(
        @Body request: CreateClienteRequest
    ): Response<ClienteResponse>

    /**
     * PATCH /cliente/{id}
     * Actualizar un cliente
     *
     * @param id ID del cliente
     * @param request Datos a actualizar
     * @return Cliente actualizado
     */
    @PATCH("cliente/{id}")
    suspend fun updateCliente(
        @Path("id") id: String,
        @Body request: UpdateClienteRequest
    ): Response<ClienteResponse>

    /**
     * DELETE /cliente/{id}
     * Eliminar un cliente
     *
     * @param id ID del cliente
     * @return Confirmación de eliminación
     */
    @DELETE("cliente/{id}")
    suspend fun deleteCliente(
        @Path("id") id: String
    ): Response<ClienteResponse>

    /**
     * POST /cliente/{id}/upload-image
     * Subir imagen a un cliente
     *
     * @param id ID del cliente
     * @param image Archivo de imagen (multipart/form-data)
     * @return Cliente con imagen actualizada
     */
    @Multipart
    @POST("cliente/{id}/upload-image")
    suspend fun uploadImage(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<ClienteResponse>
}