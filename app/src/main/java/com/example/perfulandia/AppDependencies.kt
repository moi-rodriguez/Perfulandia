package com.example.perfulandia

import android.content.Context
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.ApiService
import com.example.perfulandia.data.repository.*

/**
 * Contenedor de Dependencias (Service Locator).
 * Inicializa y mantiene las instancias únicas (Singletons) de los repositorios y APIs.
 */
class AppDependencies(context: Context) {

    // 1. Session Manager
    val sessionManager: SessionManager by lazy {
        SessionManager(context)
    }

    // 2. APIs (Retrofit)
    private val authApi by lazy { ApiService.provideAuthApi(context) }
    private val perfumeApi by lazy { ApiService.providePerfumeApi(context) }
    private val categoriaApi by lazy { ApiService.provideCategoriaApi(context) }
    private val pedidoApi by lazy { ApiService.providePedidoApi(context) }
    private val resenaApi by lazy { ApiService.provideResenaApi(context) }
    // private val clienteApi by lazy { ApiService.provideClienteApi(context) } // Si lo llegamos a usar

    // 3. Repositorios
    val authRepository: AuthRepository by lazy {
        AuthRepository(authApi, sessionManager)
    }

    val avatarRepository: AvatarRepository by lazy {
        AvatarRepository(context)
    }

    val perfumeRepository: PerfumeRepository by lazy {
        PerfumeRepository(perfumeApi)
    }

    val categoriaRepository: CategoriaRepository by lazy {
        CategoriaRepository(categoriaApi)
    }

    val pedidoRepository: PedidoRepository by lazy {
        PedidoRepository(pedidoApi)
    }

    val resenaRepository: ResenaRepository by lazy {
        ResenaRepository(resenaApi)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDependencies? = null

        fun getInstance(context: Context): AppDependencies {
            return INSTANCE ?: synchronized(this) {
                val instance = AppDependencies(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}