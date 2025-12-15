package com.example.perfulandia

import android.content.Context
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.ApiService
import com.example.perfulandia.data.repository.*
import com.example.perfulandia.viewmodel.CartViewModel
import com.example.perfulandia.viewmodel.CreateReviewViewModelFactory
import com.example.perfulandia.viewmodel.HomeViewModelFactory
import com.example.perfulandia.viewmodel.OrderViewModelFactory
import com.example.perfulandia.viewmodel.PerfumeDetailViewModelFactory

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


    // 3. Repositorios - Se inyectan las APIS correspondientes
    val authRepository: AuthRepository by lazy {
        AuthRepository(authApi, sessionManager)
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

    // 4. ViewModels
    // Se crea una instancia única (singleton) de CartViewModel para que el estado del
    // carrito se comparta entre todas las pantallas que lo necesiten (Detail, Cart, etc.).
    val cartViewModel: CartViewModel by lazy {
        CartViewModel(orderRepository, sessionManager)
    }

    // Se proporcionan las fábricas para los ViewModels que tienen dependencias.
    val homeViewModelFactory: HomeViewModelFactory by lazy {
        HomeViewModelFactory(perfumeRepository, categoryRepository)
    }

    val perfumeDetailViewModelFactory by lazy {
        PerfumeDetailViewModelFactory(perfumeRepository, reviewRepository, sessionManager, cartViewModel)
    }

    val orderViewModelFactory: OrderViewModelFactory by lazy {
        OrderViewModelFactory(orderRepository)
    }

    val createReviewViewModelFactory: CreateReviewViewModelFactory by lazy {
        CreateReviewViewModelFactory(reviewRepository)
    }

    val createPerfumeViewModelFactory by lazy {
        com.example.perfulandia.viewmodel.CreatePerfumeViewModelFactory(perfumeRepository, categoryRepository, sessionManager)
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