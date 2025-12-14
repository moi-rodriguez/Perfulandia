package com.example.perfulandia.data.remote

import android.content.Context
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.api.CategoryApi
import com.example.perfulandia.data.remote.api.OrderApi
import com.example.perfulandia.data.remote.api.PerfumeApi
import com.example.perfulandia.data.remote.api.ReviewApi
import com.example.perfulandia.data.remote.api.UserApi
import retrofit2.Retrofit

/**
 * ApiService: Singleton que proporciona todas las interfaces API
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

    fun provideAuthApi(context: Context): AuthApi {
        return getRetrofit(context).create(AuthApi::class.java)
    }

    fun providePerfumeApi(context: Context): PerfumeApi {
        return getRetrofit(context).create(PerfumeApi::class.java)
    }

    fun provideUserApi(context: Context): UserApi { // Added
        return getRetrofit(context).create(UserApi::class.java)
    }

    // NUEVAS APIS
    fun provideCategoryApi(context: Context): CategoryApi {
        return getRetrofit(context).create(CategoryApi::class.java)
    }

    fun provideOrderApi(context: Context): OrderApi {
        return getRetrofit(context).create(OrderApi::class.java)
    }

    fun provideReviewApi(context: Context): ReviewApi {
        return getRetrofit(context).create(ReviewApi::class.java)
    }
}