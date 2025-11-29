package com.example.perfulandia.data.repository

import com.example.perfulandia.data.remote.api.PerfumeApi
import com.example.perfulandia.data.remote.dto.CreatePerfumeRequest
import com.example.perfulandia.data.remote.dto.PerfumeDto
import com.example.perfulandia.data.remote.dto.UpdatePerfumeRequest
import okhttp3.MultipartBody

/**
 * PerfumeRepository: Maneja operaciones CRUD de perfumes
 */
class PerfumeRepository(
    private val perfumeApi: PerfumeApi
) {

    /**
     * Obtener todos los perfumes
     */
    suspend fun getAllPerfumes(): Result<List<PerfumeDto>> {
        return try {
            val response = perfumeApi.getAllPerfumes()

            if (response.isSuccessful && response.body() != null) {
                val perfumesResponse = response.body()!!

                if (perfumesResponse.success) {
                    Result.success(perfumesResponse.data)
                } else {
                    Result.failure(Exception(perfumesResponse.message ?: "Error al obtener perfumes"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Obtener un perfume por ID
     */
    suspend fun getPerfumeById(id: String): Result<PerfumeDto> {
        return try {
            val response = perfumeApi.getPerfumeById(id)

            if (response.isSuccessful && response.body() != null) {
                val perfumeResponse = response.body()!!

                if (perfumeResponse.success) {
                    Result.success(perfumeResponse.data)
                } else {
                    Result.failure(Exception(perfumeResponse.message ?: "Perfume no encontrado"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Crear un nuevo perfume
     */
    suspend fun createPerfume(nombre: String, descripcion: String? = null): Result<PerfumeDto> {
        return try {
            val request = CreatePerfumeRequest(nombre, descripcion)
            val response = perfumeApi.createPerfume(request)

            if (response.isSuccessful && response.body() != null) {
                val perfumeResponse = response.body()!!

                if (perfumeResponse.success) {
                    Result.success(perfumeResponse.data)
                } else {
                    Result.failure(Exception(perfumeResponse.message ?: "Error al crear perfume"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Actualizar un perfume
     */
    suspend fun updatePerfume(
        id: String,
        nombre: String? = null,
        descripcion: String? = null
    ): Result<PerfumeDto> {
        return try {
            val request = UpdatePerfumeRequest(nombre, descripcion)
            val response = perfumeApi.updatePerfume(id, request)

            if (response.isSuccessful && response.body() != null) {
                val perfumeResponse = response.body()!!

                if (perfumeResponse.success) {
                    Result.success(perfumeResponse.data)
                } else {
                    Result.failure(Exception(perfumeResponse.message ?: "Error al actualizar perfume"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Eliminar un perfume
     */
    suspend fun deletePerfume(id: String): Result<Boolean> {
        return try {
            val response = perfumeApi.deletePerfume(id)

            if (response.isSuccessful && response.body() != null) {
                val perfumeResponse = response.body()!!

                if (perfumeResponse.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(perfumeResponse.message ?: "Error al eliminar perfume"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Subir imagen a un perfume
     */
    suspend fun uploadImage(id: String, image: MultipartBody.Part): Result<PerfumeDto> {
        return try {
            val response = perfumeApi.uploadImage(id, image)

            if (response.isSuccessful && response.body() != null) {
                val perfumeResponse = response.body()!!

                if (perfumeResponse.success) {
                    Result.success(perfumeResponse.data)
                } else {
                    Result.failure(Exception(perfumeResponse.message ?: "Error al subir imagen"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}