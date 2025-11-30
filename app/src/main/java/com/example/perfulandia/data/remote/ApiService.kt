package com.example.perfulandia.data.remote

import android.content.Context
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.api.CategoriaApi
import com.example.perfulandia.data.remote.api.ClienteApi
import com.example.perfulandia.data.remote.api.PedidoApi
import com.example.perfulandia.data.remote.api.PerfumeApi
import com.example.perfulandia.data.remote.api.ResenaApi
import com.example.perfulandia.data.remote.api.UploadApi
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

    /**
     * CategoriaApi: CRUD de categorías
     *
     * Endpoints:
     * - GET /categoria
     * - POST /categoria
     * - GET /categoria/{id}
     * - PATCH /categoria/{id}
     * - DELETE /categoria/{id}
     * - POST /categoria/{id}/upload-image
     */
    fun provideCategoriaApi(context: Context): CategoriaApi {
        return getRetrofit(context).create(CategoriaApi::class.java)
    }

    /**
     * ClienteApi: CRUD de clientes
     *
     * Endpoints:
     * - GET /cliente
     * - POST /cliente
     * - GET /cliente/{id}
     * - PATCH /cliente/{id}
     * - DELETE /cliente/{id}
     * - POST /cliente/{id}/upload-image
     */
    fun provideClienteApi(context: Context): ClienteApi {
        return getRetrofit(context).create(ClienteApi::class.java)
    }

    /**
     * PedidoApi: CRUD de pedidos
     *
     * Endpoints:
     * - GET /pedido
     * - POST /pedido
     * - GET /pedido/{id}
     * - PATCH /pedido/{id}
     * - DELETE /pedido/{id}
     * - POST /pedido/{id}/upload-image
     */
    fun providePedidoApi(context: Context): PedidoApi {
        return getRetrofit(context).create(PedidoApi::class.java)
    }

    /**
     * ResenaApi: CRUD de reseñas
     *
     * Endpoints:
     * - GET /resena
     * - POST /resena
     * - GET /resena/{id}
     * - PATCH /resena/{id}
     * - DELETE /resena/{id}
     * - POST /resena/{id}/upload-image
     */
    fun provideResenaApi(context: Context): ResenaApi {
        return getRetrofit(context).create(ResenaApi::class.java)
    }

    /**
     * UploadApi: Subida de imágenes
     *
     * Endpoints:
     * - POST /upload/image
     */
    fun provideUploadApi(context: Context): UploadApi {
        return getRetrofit(context).create(UploadApi::class.java)
    }
}

/*
 * ============================================
 * FLUJO DE AUTENTICACIÓN
 * ============================================
 *
 * 1. Usuario ingresa email/password en LoginScreen
 *    ↓
 * 2. LoginViewModel llama al AuthRepository
 *    ↓
 * 3. AuthRepository usa ApiService.provideAuthApi(context).login(...)
 *    ↓
 * 4. Servidor valida credenciales y devuelve AuthResponse con token y user
 *    ↓
 * 5. SessionManager guarda: token, userId, userName, userEmail, userRole
 *    ↓
 * 6. AuthInterceptor añade automáticamente el token a futuras peticiones
 *    ↓
 * 7. Usuario puede acceder a endpoints protegidos sin pasar token manualmente
 *
 * ============================================
 * EJEMPLO DE USO EN REPOSITORIES
 * ============================================
 *
 * class PerfumeRepository(private val context: Context) {
 *
 *     private val api = ApiService.providePerfumeApi(context)
 *     private val mapper = PerfumeMapper
 *
 *     suspend fun getAllPerfumes(): Resource<List<Perfume>> {
 *         return try {
 *             val response = api.getPerfumes()
 *             if (response.isSuccessful && response.body() != null) {
 *                 val perfumes = mapper.fromDtoList(response.body()!!.data)
 *                 Resource.Success(perfumes)
 *             } else {
 *                 Resource.Error("Error al obtener perfumes")
 *             }
 *         } catch (e: Exception) {
 *             Resource.Error(e.message ?: "Error desconocido")
 *         }
 *     }
 * }
 */