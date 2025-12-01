package com.example.perfulandia.data.repository

import com.example.perfulandia.data.mapper.PerfumeMapper
import com.example.perfulandia.data.remote.api.PerfumeApi
import com.example.perfulandia.data.remote.dto.CreatePerfumeRequest
import com.example.perfulandia.data.remote.dto.UpdatePerfumeRequest
import com.example.perfulandia.model.Perfume
import okhttp3.MultipartBody

/**
 * PerfumeRepository: Gestiona los datos de los perfumes.
 *
 * Actúa como intermediario entre la API (datos crudos) y el ViewModel (datos de dominio).
 * Se encarga de manejar las respuestas HTTP y convertir los DTOs a modelos limpios.
 */
class PerfumeRepository(
    private val api: PerfumeApi
) {

    /**
     * Obtiene la lista de todos los perfumes.
     * Convierte List<PerfumeDto> -> List<Perfume>
     */
    suspend fun getAllPerfumes(): Result<List<Perfume>> {
        return try {
            val response = api.getAllPerfumes()

            if (response.isSuccessful) {
                val body = response.body()
                // Verificamos que el cuerpo no sea nulo y que el flag 'success' sea true
                if (body != null && body.success) {
                    // // Transformación de DTO (Capa de Red) a Modelo (Capa de Dominio) usando Mapper.
                    val perfumesDomain = PerfumeMapper.fromDtoList(body.data)
                    Result.success(perfumesDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener la lista de perfumes"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene un perfume específico por su ID.
     * Convierte PerfumeDto -> Perfume
     */
    suspend fun getPerfumeById(id: String): Result<Perfume> {
        return try {
            val response = api.getPerfumeById(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    // Convertimos el DTO individual a Modelo de Dominio
                    val perfumeDomain = PerfumeMapper.fromDto(body.data)
                    Result.success(perfumeDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Perfume no encontrado"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crea un nuevo perfume.
     */
    suspend fun createPerfume(nombre: String, descripcion: String?): Result<Perfume> {
        return try {
            val request = CreatePerfumeRequest(nombre, descripcion)
            val response = api.createPerfume(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(PerfumeMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al crear el perfume"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza un perfume existente.
     */
    suspend fun updatePerfume(id: String, nombre: String?, descripcion: String?): Result<Perfume> {
        return try {
            val request = UpdatePerfumeRequest(nombre, descripcion)
            val response = api.updatePerfume(id, request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(PerfumeMapper.fromDto(body.data))
                } else {
                    Result.failure(Exception(body?.message ?: "Error al actualizar el perfume"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina un perfume por ID.
     */
    suspend fun deletePerfume(id: String): Result<Boolean> {
        return try {
            val response = api.deletePerfume(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al eliminar el perfume"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sube una imagen para un perfume.
     */
    suspend fun uploadImage(id: String, imagePart: MultipartBody.Part): Result<Perfume> {
        return try {
            val response = api.uploadImage(id, imagePart)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(PerfumeMapper.fromDto(body.data))
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