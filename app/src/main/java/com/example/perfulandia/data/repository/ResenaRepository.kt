package com.example.perfulandia.data.repository

import com.example.perfulandia.data.remote.api.ResenaApi
import com.example.perfulandia.data.remote.dto.ResenaDto
import com.example.perfulandia.data.remote.dto.CreateResenaRequest
import com.example.perfulandia.data.remote.dto.UpdateResenaRequest
import okhttp3.MultipartBody

/**
 * ResenaRepository: Maneja operaciones CRUD de reseñas
 */
class ResenaRepository(
    private val resenaApi: ResenaApi
) {

    /**
     * Obtener todas las reseñas
     */
    suspend fun getAllResenas(): Result<List<ResenaDto>> {
        return try {
            val response = resenaApi.getAllResenas()

            if (response.isSuccessful && response.body() != null) {
                val resenasResponse = response.body()!!

                if (resenasResponse.success) {
                    Result.success(resenasResponse.data)
                } else {
                    Result.failure(Exception(resenasResponse.message ?: "Error al obtener reseñas"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Obtener una reseña por ID
     */
    suspend fun getResenaById(id: String): Result<ResenaDto> {
        return try {
            val response = resenaApi.getResenaById(id)

            if (response.isSuccessful && response.body() != null) {
                val resenaResponse = response.body()!!

                if (resenaResponse.success) {
                    Result.success(resenaResponse.data)
                } else {
                    Result.failure(Exception(resenaResponse.message ?: "Reseña no encontrada"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Crear una nueva reseña
     */
    suspend fun createResena(nombre: String, descripcion: String? = null): Result<ResenaDto> {
        return try {
            val request = CreateResenaRequest(nombre, descripcion)
            val response = resenaApi.createResena(request)

            if (response.isSuccessful && response.body() != null) {
                val resenaResponse = response.body()!!

                if (resenaResponse.success) {
                    Result.success(resenaResponse.data)
                } else {
                    Result.failure(Exception(resenaResponse.message ?: "Error al crear reseña"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Actualizar una reseña
     */
    suspend fun updateResena(
        id: String,
        nombre: String? = null,
        descripcion: String? = null
    ): Result<ResenaDto> {
        return try {
            val request = UpdateResenaRequest(nombre, descripcion)
            val response = resenaApi.updateResena(id, request)

            if (response.isSuccessful && response.body() != null) {
                val resenaResponse = response.body()!!

                if (resenaResponse.success) {
                    Result.success(resenaResponse.data)
                } else {
                    Result.failure(Exception(resenaResponse.message ?: "Error al actualizar reseña"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Eliminar una reseña
     */
    suspend fun deleteResena(id: String): Result<Boolean> {
        return try {
            val response = resenaApi.deleteResena(id)

            if (response.isSuccessful && response.body() != null) {
                val resenaResponse = response.body()!!

                if (resenaResponse.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(resenaResponse.message ?: "Error al eliminar reseña"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Subir imagen a una reseña
     */
    suspend fun uploadImage(id: String, image: MultipartBody.Part): Result<ResenaDto> {
        return try {
            val response = resenaApi.uploadImage(id, image)

            if (response.isSuccessful && response.body() != null) {
                val resenaResponse = response.body()!!

                if (resenaResponse.success) {
                    Result.success(resenaResponse.data)
                } else {
                    Result.failure(Exception(resenaResponse.message ?: "Error al subir imagen"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}