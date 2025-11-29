package com.example.perfulandia.data.repository

import com.example.perfulandia.data.remote.api.PedidoApi
import com.example.perfulandia.data.remote.dto.PedidoDto
import com.example.perfulandia.data.remote.dto.CreatePedidoRequest
import com.example.perfulandia.data.remote.dto.UpdatePedidoRequest
import okhttp3.MultipartBody

/**
 * PedidoRepository: Maneja operaciones CRUD de pedidos
 */
class PedidoRepository(
    private val pedidoApi: PedidoApi
) {

    /**
     * Obtener todos los pedidos
     */
    suspend fun getAllPedidos(): Result<List<PedidoDto>> {
        return try {
            val response = pedidoApi.getAllPedidos()

            if (response.isSuccessful && response.body() != null) {
                val pedidosResponse = response.body()!!

                if (pedidosResponse.success) {
                    Result.success(pedidosResponse.data)
                } else {
                    Result.failure(Exception(pedidosResponse.message ?: "Error al obtener pedidos"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Obtener un pedido por ID
     */
    suspend fun getPedidoById(id: String): Result<PedidoDto> {
        return try {
            val response = pedidoApi.getPedidoById(id)

            if (response.isSuccessful && response.body() != null) {
                val pedidoResponse = response.body()!!

                if (pedidoResponse.success) {
                    Result.success(pedidoResponse.data)
                } else {
                    Result.failure(Exception(pedidoResponse.message ?: "Pedido no encontrado"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Crear un nuevo pedido
     */
    suspend fun createPedido(nombre: String, descripcion: String? = null): Result<PedidoDto> {
        return try {
            val request = CreatePedidoRequest(nombre, descripcion)
            val response = pedidoApi.createPedido(request)

            if (response.isSuccessful && response.body() != null) {
                val pedidoResponse = response.body()!!

                if (pedidoResponse.success) {
                    Result.success(pedidoResponse.data)
                } else {
                    Result.failure(Exception(pedidoResponse.message ?: "Error al crear pedido"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Actualizar un pedido
     */
    suspend fun updatePedido(
        id: String,
        nombre: String? = null,
        descripcion: String? = null
    ): Result<PedidoDto> {
        return try {
            val request = UpdatePedidoRequest(nombre, descripcion)
            val response = pedidoApi.updatePedido(id, request)

            if (response.isSuccessful && response.body() != null) {
                val pedidoResponse = response.body()!!

                if (pedidoResponse.success) {
                    Result.success(pedidoResponse.data)
                } else {
                    Result.failure(Exception(pedidoResponse.message ?: "Error al actualizar pedido"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Eliminar un pedido
     */
    suspend fun deletePedido(id: String): Result<Boolean> {
        return try {
            val response = pedidoApi.deletePedido(id)

            if (response.isSuccessful && response.body() != null) {
                val pedidoResponse = response.body()!!

                if (pedidoResponse.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(pedidoResponse.message ?: "Error al eliminar pedido"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Subir imagen a un pedido
     */
    suspend fun uploadImage(id: String, image: MultipartBody.Part): Result<PedidoDto> {
        return try {
            val response = pedidoApi.uploadImage(id, image)

            if (response.isSuccessful && response.body() != null) {
                val pedidoResponse = response.body()!!

                if (pedidoResponse.success) {
                    Result.success(pedidoResponse.data)
                } else {
                    Result.failure(Exception(pedidoResponse.message ?: "Error al subir imagen"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}