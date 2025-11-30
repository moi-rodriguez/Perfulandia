package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.mapper.ResenaMapper
import com.example.perfulandia.data.remote.api.ResenaApi
import com.example.perfulandia.data.remote.dto.CreateResenaRequest
import com.example.perfulandia.data.remote.dto.UpdateResenaRequest
import com.example.perfulandia.model.Resena
import okhttp3.MultipartBody

/**
 * ResenaRepository: Gestiona los datos de las reseñas.
 *
 * Actúa como intermediario entre la API (datos crudos) y el ViewModel (datos de dominio).
 * Se encarga de manejar las respuestas HTTP y convertir los DTOs a modelos limpios.
 */
class ResenaRepository(
    private val api: ResenaApi
) {

    /**
     * Obtiene la lista de todas las reseñas.
     * Convierte List<ResenaDto> -> List<Resena>
     */
    suspend fun getAllResenas(): Result<List<Resena>> {
        return try {
            val response = api.getAllResenas()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val resenasDomain = ResenaMapper.fromDtoList(body.data)
                    Result.success(resenasDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener la lista de reseñas"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene una reseña específica por su ID.
     */
    suspend fun getResenaById(id: String): Result<Resena> {
        return try {
            val response = api.getResenaById(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val resenaDomain = ResenaMapper.fromDto(body.data)
                    Result.success(resenaDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Reseña no encontrada"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crea una nueva reseña.
     */
    suspend fun createResena(nombre: String, descripcion: String?): Result<Resena> {
        return try {
            val request = CreateResenaRequest(nombre, descripcion)
            val response = api.createResena(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(ResenaMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al crear la reseña"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza una reseña existente.
     */
    suspend fun updateResena(id: String, nombre: String?, descripcion: String?): Result<Resena> {
        return try {
            val request = UpdateResenaRequest(nombre, descripcion)
            val response = api.updateResena(id, request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(ResenaMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al actualizar la reseña"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una reseña por ID.
     */
    suspend fun deleteResena(id: String): Result<Boolean> {
        return try {
            val response = api.deleteResena(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al eliminar la reseña"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sube una imagen para una reseña.
     */
    suspend fun uploadImage(id: String, imagePart: MultipartBody.Part): Result<Resena> {
        return try {
            val response = api.uploadImage(id, imagePart)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(ResenaMapper.fromDto(body.data))
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