package com.example.perfulandia.data.repository

import com.example.perfulandia.data.mapper.PerfumeMapper
import com.example.perfulandia.data.remote.api.PerfumeApi
import com.example.perfulandia.model.Perfume

/**
 * PerfumeRepository: Gestiona los datos de los perfumes.
 *
 * Solo contiene las operaciones de lectura necesarias
 */
class PerfumeRepository(
    private val api: PerfumeApi
) {

    /**
     * Obtiene la lista de todos los perfumes.
     * Usado en: HomeScreen (HomeViewModel)
     */
    suspend fun getAllPerfumes(): Result<List<Perfume>> {
        return try {
            val response = api.getAllPerfumes()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
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
     * Obtiene un solo perfume por su ID.
     * Usado en: PerfumeDetailScreen (PerfumeDetailViewModel)
     */
    suspend fun getPerfumeById(perfumeId: String): Result<Perfume> {
        return try {
            val response = api.getPerfumeById(perfumeId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    val perfumeDomain = PerfumeMapper.fromDto(body.data)
                    Result.success(perfumeDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener el perfume"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}