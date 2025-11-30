package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.mapper.PedidoMapper
import com.example.perfulandia.data.remote.api.PedidoApi
import com.example.perfulandia.data.remote.dto.CreatePedidoRequest
import com.example.perfulandia.data.remote.dto.UpdatePedidoRequest
import com.example.perfulandia.model.Pedido
import okhttp3.MultipartBody

/**
 * PedidoRepository: Gestiona los datos de los pedidos.
 *
 * Actúa como intermediario entre la API (datos crudos) y el ViewModel (datos de dominio).
 * Se encarga de manejar las respuestas HTTP y convertir los DTOs a modelos limpios.
 */
class PedidoRepository(
    private val api: PedidoApi
) {

    /**
     * Obtiene la lista de todos los pedidos.
     * Convierte List<PedidoDto> -> List<Pedido>
     */
    suspend fun getAllPedidos(): Result<List<Pedido>> {
        return try {
            val response = api.getAllPedidos()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val pedidosDomain = PedidoMapper.fromDtoList(body.data)
                    Result.success(pedidosDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener la lista de pedidos"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene un pedido específico por su ID.
     */
    suspend fun getPedidoById(id: String): Result<Pedido> {
        return try {
            val response = api.getPedidoById(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val pedidoDomain = PedidoMapper.fromDto(body.data)
                    Result.success(pedidoDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Pedido no encontrado"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crea un nuevo pedido.
     */
    suspend fun createPedido(nombre: String, descripcion: String?): Result<Pedido> {
        return try {
            val request = CreatePedidoRequest(nombre, descripcion)
            val response = api.createPedido(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(PedidoMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al crear el pedido"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza un pedido existente.
     */
    suspend fun updatePedido(id: String, nombre: String?, descripcion: String?): Result<Pedido> {
        return try {
            val request = UpdatePedidoRequest(nombre, descripcion)
            val response = api.updatePedido(id, request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(PedidoMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al actualizar el pedido"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina un pedido por ID.
     */
    suspend fun deletePedido(id: String): Result<Boolean> {
        return try {
            val response = api.deletePedido(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al eliminar el pedido"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sube una imagen para un pedido.
     */
    suspend fun uploadImage(id: String, imagePart: MultipartBody.Part): Result<Pedido> {
        return try {
            val response = api.uploadImage(id, imagePart)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(PedidoMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al subir la imagen"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}