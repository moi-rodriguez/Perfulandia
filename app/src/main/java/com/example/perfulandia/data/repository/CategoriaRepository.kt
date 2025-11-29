package com.example.perfulandia.data.repository

import com.example.perfulandia.data.remote.api.CategoriaApi
import com.example.perfulandia.data.remote.dto.CategoriaDto
import com.example.perfulandia.data.remote.dto.CreateCategoriaRequest
import com.example.perfulandia.data.remote.dto.UpdateCategoriaRequest
import okhttp3.MultipartBody

/**
 * CategoriaRepository: Maneja operaciones CRUD de categorías
 */
class CategoriaRepository(
    private val categoriaApi: CategoriaApi
) {

    /**
     * Obtener todas las categorías
     */
    suspend fun getAllCategorias(): Result<List<CategoriaDto>> {
        return try {
            val response = categoriaApi.getAllCategorias()

            if (response.isSuccessful && response.body() != null) {
                val categoriasResponse = response.body()!!

                if (categoriasResponse.success) {
                    Result.success(categoriasResponse.data)
                } else {
                    Result.failure(Exception(categoriasResponse.message ?: "Error al obtener categorías"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Obtener una categoría por ID
     */
    suspend fun getCategoriaById(id: String): Result<CategoriaDto> {
        return try {
            val response = categoriaApi.getCategoriaById(id)

            if (response.isSuccessful && response.body() != null) {
                val categoriaResponse = response.body()!!

                if (categoriaResponse.success) {
                    Result.success(categoriaResponse.data)
                } else {
                    Result.failure(Exception(categoriaResponse.message ?: "Categoría no encontrada"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Crear una nueva categoría
     */
    suspend fun createCategoria(nombre: String, descripcion: String? = null): Result<CategoriaDto> {
        return try {
            val request = CreateCategoriaRequest(nombre, descripcion)
            val response = categoriaApi.createCategoria(request)

            if (response.isSuccessful && response.body() != null) {
                val categoriaResponse = response.body()!!

                if (categoriaResponse.success) {
                    Result.success(categoriaResponse.data)
                } else {
                    Result.failure(Exception(categoriaResponse.message ?: "Error al crear categoría"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Actualizar una categoría
     */
    suspend fun updateCategoria(
        id: String,
        nombre: String? = null,
        descripcion: String? = null
    ): Result<CategoriaDto> {
        return try {
            val request = UpdateCategoriaRequest(nombre, descripcion)
            val response = categoriaApi.updateCategoria(id, request)

            if (response.isSuccessful && response.body() != null) {
                val categoriaResponse = response.body()!!

                if (categoriaResponse.success) {
                    Result.success(categoriaResponse.data)
                } else {
                    Result.failure(Exception(categoriaResponse.message ?: "Error al actualizar categoría"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Eliminar una categoría
     */
    suspend fun deleteCategoria(id: String): Result<Boolean> {
        return try {
            val response = categoriaApi.deleteCategoria(id)

            if (response.isSuccessful && response.body() != null) {
                val categoriaResponse = response.body()!!

                if (categoriaResponse.success) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(categoriaResponse.message ?: "Error al eliminar categoría"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Subir imagen a una categoría
     */
    suspend fun uploadImage(id: String, image: MultipartBody.Part): Result<CategoriaDto> {
        return try {
            val response = categoriaApi.uploadImage(id, image)

            if (response.isSuccessful && response.body() != null) {
                val categoriaResponse = response.body()!!

                if (categoriaResponse.success) {
                    Result.success(categoriaResponse.data)
                } else {
                    Result.failure(Exception(categoriaResponse.message ?: "Error al subir imagen"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}