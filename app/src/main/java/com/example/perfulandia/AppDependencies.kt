package com.example.perfulandia

import android.content.Context
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.ApiService
import com.example.perfulandia.data.repository.*
import com.example.perfulandia.viewmodel.HomeViewModelFactory

/**
 * Service Locator: Contenedor de todas las dependencias de la app
 * Inicializa y mantiene las instancias únicas (Singletons) de los repositorios y APIs.
 */
class AppDependencies(context: Context) {

    // 1. Session Manager
    val sessionManager: SessionManager by lazy {
        SessionManager(context)
    }

    // 2. APIs (Retrofit) - Obtenemos las instancias desde ApiService
    private val authApi by lazy { ApiService.provideAuthApi(context) }
    private val perfumeApi by lazy { ApiService.providePerfumeApi(context) }
    private val categoryApi by lazy { ApiService.provideCategoryApi(context) }
    private val orderApi by lazy { ApiService.provideOrderApi(context) }
    private val reviewApi by lazy { ApiService.provideReviewApi(context) }
    private val userApi by lazy { ApiService.provideUserApi(context) } // Added


    // 3. Repositorios - Se inyectan las APIS correspondientes
    val authRepository: AuthRepository by lazy {
        AuthRepository(authApi, sessionManager)
    }

    val userRepository: UserRepository by lazy { // Added
        UserRepository(userApi)
    }

    val avatarRepository: AvatarRepository by lazy {
        AvatarRepository(context)
    }

    val perfumeRepository: PerfumeRepository by lazy {
        PerfumeRepository(perfumeApi)
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(categoryApi)
    }

    val orderRepository: OrderRepository by lazy {
        OrderRepository(orderApi)
    }

    val reviewRepository: ReviewRepository by lazy {
        ReviewRepository(reviewApi)
    }

    // 4. ViewModels (Factory Providers)
    // Se proporcionan las fábricas para los ViewModels que tienen dependencias en su constructor.
    // Esto es parte del patrón de inyección manual de dependencias (Service Locator)
    // para asegurar que los ViewModels se construyan correctamente y sus ciclos de vida sean manejados por Android.
    val homeViewModelFactory: HomeViewModelFactory by lazy {
        HomeViewModelFactory(perfumeRepository, categoryRepository)
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