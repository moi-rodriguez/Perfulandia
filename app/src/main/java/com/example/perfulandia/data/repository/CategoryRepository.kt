package com.example.perfulandia.data.repository

import com.example.perfulandia.data.mapper.CategoryMapper
import com.example.perfulandia.data.remote.api.CategoryApi
import com.example.perfulandia.model.Category

class CategoryRepository(private val api: CategoryApi) {

    suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val response = api.getAllCategories()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    val domainCategories = CategoryMapper.fromDtoList(body.data)
                    Result.success(domainCategories)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener las categorías"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}