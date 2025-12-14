package com.example.perfulandia.data.repository

import com.example.perfulandia.data.remote.api.CategoryApi
import com.example.perfulandia.data.remote.dto.CategoriesResponse

class CategoryRepository(private val api: CategoryApi) {

    // Funcion simple para conectar con la API
    suspend fun getAllCategories(): Result<CategoriesResponse> {
        return try {
            val response = api.getAllCategories()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener categorias"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}