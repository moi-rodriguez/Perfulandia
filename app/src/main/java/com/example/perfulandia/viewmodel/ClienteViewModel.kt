package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.mapper.ClienteMapper
import com.example.perfulandia.data.remote.api.ClienteApi
import com.example.perfulandia.data.remote.dto.CreateClienteRequest
import com.example.perfulandia.data.remote.dto.UpdateClienteRequest
import com.example.perfulandia.model.Cliente
import okhttp3.MultipartBody

/**
 * ClienteRepository: Gestiona los datos de los clientes.
 *
 * Actúa como intermediario entre la API (datos crudos) y el ViewModel (datos de dominio).
 * Se encarga de manejar las respuestas HTTP y convertir los DTOs a modelos limpios.
 */
class ClienteRepository(
    private val api: ClienteApi
) {

    /**
     * Obtiene la lista de todos los clientes.
     * Convierte List<ClienteDto> -> List<Cliente>
     */
    suspend fun getAllClientes(): Result<List<Cliente>> {
        return try {
            val response = api.getAllClientes()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val clientesDomain = ClienteMapper.fromDtoList(body.data)
                    Result.success(clientesDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener la lista de clientes"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene un cliente específico por su ID.
     */
    suspend fun getClienteById(id: String): Result<Cliente> {
        return try {
            val response = api.getClienteById(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val clienteDomain = ClienteMapper.fromDto(body.data)
                    Result.success(clienteDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Cliente no encontrado"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crea un nuevo cliente.
     */
    suspend fun createCliente(nombre: String, descripcion: String?): Result<Cliente> {
        return try {
            val request = CreateClienteRequest(nombre, descripcion)
            val response = api.createCliente(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(ClienteMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al crear el cliente"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza un cliente existente.
     */
    suspend fun updateCliente(id: String, nombre: String?, descripcion: String?): Result<Cliente> {
        return try {
            val request = UpdateClienteRequest(nombre, descripcion)
            val response = api.updateCliente(id, request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(ClienteMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al actualizar el cliente"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina un cliente por ID.
     */
    suspend fun deleteCliente(id: String): Result<Boolean> {
        return try {
            val response = api.deleteCliente(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al eliminar el cliente"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sube una imagen para un cliente.
     */
    suspend fun uploadImage(id: String, imagePart: MultipartBody.Part): Result<Cliente> {
        return try {
            val response = api.uploadImage(id, imagePart)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(ClienteMapper.fromDto(body.data))
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