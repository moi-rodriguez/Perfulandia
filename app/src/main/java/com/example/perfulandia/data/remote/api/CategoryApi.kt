package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.CategoriesResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interfaz para pedir las categorias al servidor
 */
interface CategoryApi {

    @GET("categoria")
    suspend fun getAllCategories(): Response<CategoriesResponse>
}