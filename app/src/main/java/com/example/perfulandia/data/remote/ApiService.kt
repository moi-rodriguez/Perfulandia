package com.example.perfulandia.data.remote

import android.content.Context
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.api.PerfumeApi
import retrofit2.Retrofit

/**
 * ApiService: Singleton que proporciona todas las interfaces API
 *
 * Centraliza la creación de servicios Retrofit para:
 * - AuthApi (autenticación y usuarios)
 * - PerfumeApi (CRUD de perfumes)
 * - CategoriaApi (CRUD de categorías)
 * - ClienteApi (CRUD de clientes)
 * - PedidoApi (CRUD de pedidos)
 * - ResenaApi (CRUD de reseñas)
 * - UploadApi (subida de imágenes)
 *
 * Uso en Repositories:
 * ```
 * class PerfumeRepository(context: Context) {
 *     private val api = ApiService.providePerfumeApi(context)
 * }
 * ```
 */
object ApiService {

    // Instancia única de Retrofit (lazy initialization)
    private var retrofit: Retrofit? = null

    /**
     * Inicializa Retrofit con el contexto de la aplicación
     * Se llama automáticamente al obtener cualquier API
     */
    private fun getRetrofit(context: Context): Retrofit {
        if (retrofit == null) {
            retrofit = RetrofitClient.create(context.applicationContext)
        }
        return retrofit!!
    }

    /**
     * Resetea la instancia de Retrofit
     * Útil para testing o cuando cambias de usuario/configuración
     */
    fun reset() {
        retrofit = null
    }

    // ============================================
    // APIs - Proporcionan las interfaces de Retrofit
    // ============================================

    /**
     * AuthApi: Autenticación y gestión de usuarios
     *
     * Endpoints:
     * - POST /auth/login
     * - POST /auth/register
     * - GET /auth/profile
     * - GET /auth/users
     */
    fun provideAuthApi(context: Context): AuthApi {
        return getRetrofit(context).create(AuthApi::class.java)
    }

    /**
     * PerfumeApi: CRUD de perfumes
     *
     * Endpoints:
     * - GET /perfume
     * - POST /perfume
     * - GET /perfume/{id}
     * - PATCH /perfume/{id}
     * - DELETE /perfume/{id}
     * - POST /perfume/{id}/upload-image
     */
    fun providePerfumeApi(context: Context): PerfumeApi {
        return getRetrofit(context).create(PerfumeApi::class.java)
    }
}