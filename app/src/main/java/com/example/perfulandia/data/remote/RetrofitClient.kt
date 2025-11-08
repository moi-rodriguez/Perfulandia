package com.example.perfulandia.data.remote

import android.content.Context
import com.example.perfulandia.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitClient: Configura Retrofit con interceptores y base URL
 */
object RetrofitClient {

    // API
    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW/"

    /**
     * Inicializa Retrofit con el contexto de la app.
     * Se recomienda llamar una sola vez (por ejemplo desde un ViewModel o Singleton Repository)
     */
    fun create(context: Context): Retrofit {

        // 1. SessionManager para manejar el token
        val sessionManager = SessionManager(context)

        // 2. AuthInterceptor para inyectar el token automáticamente
        val authInterceptor = AuthInterceptor(sessionManager)

        // 3. HttpLoggingInterceptor para ver peticiones/respuestas en Logcat
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Cambia a NONE en producción
        }

        // 4. Configurar OkHttpClient con ambos interceptores
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)     // Inyecta el token
            .addInterceptor(loggingInterceptor)  // Log de peticiones/respuestas
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        // 5. Construir Retrofit con GsonConverter y el cliente configurado
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}
