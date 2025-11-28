package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.PedidoResponse
import com.example.perfulandia.data.remote.dto.PedidosResponse
import com.example.perfulandia.data.remote.dto.CreatePedidoRequest
import com.example.perfulandia.data.remote.dto.UpdatePedidoRequest
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
 * PedidoApi: Endpoints de pedidos
 * Base URL: /pedido
 * Todos los endpoints requieren autenticación (Token JWT)
 */
interface PedidoApi {

    /**
     * GET /pedido
     * Listar todos los pedidos
     *
     * @return Lista de pedidos
     */
    @GET("pedido")
    suspend fun getAllPedidos(): Response<PedidosResponse>

    /**
     * GET /pedido/{id}
     * Obtener un pedido por ID
     *
     * @param id ID del pedido
     * @return Datos del pedido
     */
    @GET("pedido/{id}")
    suspend fun getPedidoById(
        @Path("id") id: String
    ): Response<PedidoResponse>

    /**
     * POST /pedido
     * Crear un nuevo pedido
     *
     * @param request Datos del pedido (nombre, descripcion)
     * @return Pedido creado
     */
    @POST("pedido")
    suspend fun createPedido(
        @Body request: CreatePedidoRequest
    ): Response<PedidoResponse>

    /**
     * PATCH /pedido/{id}
     * Actualizar un pedido
     *
     * @param id ID del pedido
     * @param request Datos a actualizar
     * @return Pedido actualizado
     */
    @PATCH("pedido/{id}")
    suspend fun updatePedido(
        @Path("id") id: String,
        @Body request: UpdatePedidoRequest
    ): Response<PedidoResponse>

    /**
     * DELETE /pedido/{id}
     * Eliminar un pedido
     *
     * @param id ID del pedido
     * @return Confirmación de eliminación
     */
    @DELETE("pedido/{id}")
    suspend fun deletePedido(
        @Path("id") id: String
    ): Response<PedidoResponse>

    /**
     * POST /pedido/{id}/upload-image
     * Subir imagen a un pedido
     *
     * @param id ID del pedido
     * @param image Archivo de imagen (multipart/form-data)
     * @return Pedido con imagen actualizada
     */
    @Multipart
    @POST("pedido/{id}/upload-image")
    suspend fun uploadImage(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<PedidoResponse>
}