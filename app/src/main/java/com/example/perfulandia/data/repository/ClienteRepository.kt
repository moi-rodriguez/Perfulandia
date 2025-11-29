package com.example.perfulandia.data.repository

import com.example.perfulandia.data.remote.api.ClienteApi
import com.example.perfulandia.data.remote.dto.ClienteDto
import com.example.perfulandia.data.remote.dto.CreateClienteRequest
import com.example.perfulandia.data.remote.dto.UpdateClienteRequest
import okhttp3.MultipartBody

/**
 * ClienteRepository: Maneja operaciones CRUD de clientes
 */
class ClienteRepository(
    private val clienteApi: ClienteApi
) {

    /**
     * Obtener todos los clientes
     */
    suspend fun getAllClientes(): Result<List<ClienteDto>> {
        return try {
            val response = clienteApi.getAllClientes()

            if (response.isSuccessful && response.body() != null) {
                val clientesResponse = response.body()!!

                if (clientesResponse.success) {
                    Result.success(clientesResponse.data)
                } else {
                    Result.failure(Exception(clientesResponse.message ?: "Error al obtener clientes"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Obtener un cliente por ID
     */
    suspend fun getClienteById(id: String): Result<ClienteDto> {
        return try {
            val response = clienteApi.getClienteById(id)

            if (response.isSuccessful && response.body() != null) {
                val clienteResponse = response.body()!!

                if (clienteResponse.success) {
                    Result.success(clienteResponse.data)
                } else {
                    Result.failure(Exception(clienteResponse.message ?: "Cliente no encontrado"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Crear un nuevo cliente
     */
    suspend fun createCliente(nombre: String, descripcion: String? = null): Result<ClienteDto> {
        return try {
            val request = CreateClienteRequest(nombre, descripcion)
            val response = clienteApi.createCliente(request)

            if (response.isSuccessful && response.body() != null) {
                val clienteResponse = response.body()!!

                if (clienteResponse.success) {
                    Result.success(clienteResponse.data)
                } else {
                    Result.failure(Exception(clienteResponse.message ?: "Error al crear cliente"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Actualizar un cliente
     */
    suspend fun updateCliente(
        id: String,
        nombre: String? = null,
        descripcion: String? = null
    ): Result<ClienteDto> {
        return try {
            val request = UpdateClienteRequest(nombre, descripcion)
            val response = clienteApi.updateCliente(id, request)

            if (response.isSuccessful && response.body() != null) {
                val clienteResponse = response.body()!!

                if (clienteResponse.success) {
                    Result.success(clienteResponse.data)
                } else {
                    Result.failure(Exception(clienteResponse.message ?: "Error al actualizar cliente"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Eliminar un cliente
     */
    suspend fun deleteCliente(id: String): Result<Boolean> {
        return try {
            val response = clienteApi.deleteCliente(id)

            if (response.isSuccessful && response.body() != null) {
                val clienteResponse = response.body()!!

                if (clienteResponse.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(clienteResponse.message ?: "Error al eliminar cliente"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Subir imagen a un cliente
     */
    suspend fun uploadImage(id: String, image: MultipartBody.Part): Result<ClienteDto> {
        return try {
            val response = clienteApi.uploadImage(id, image)

            if (response.isSuccessful && response.body() != null) {
                val clienteResponse = response.body()!!

                if (clienteResponse.success) {
                    Result.success(clienteResponse.data)
                } else {
                    Result.failure(Exception(clienteResponse.message ?: "Error al subir imagen"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}