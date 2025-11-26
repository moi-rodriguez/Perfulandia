package com.example.perfulandia.data.remote.interceptor

import com.example.perfulandia.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

/**
 * AuthInterceptor: Añade automáticamente el token JWT a las peticiones
 * y maneja respuestas de autenticación
 *
 * Funcionalidades:
 * - Añade header "Authorization: Bearer {token}" a peticiones autenticadas
 * - Excluye endpoints públicos que no necesitan autenticación
 * - Maneja respuestas 401 (Unauthorized) limpiando la sesión
 * - Añade headers adicionales requeridos por la API
 */
class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {

    companion object {
        // Endpoints que NO requieren autenticación
        private val PUBLIC_ENDPOINTS = setOf(
            "/auth/login",
            "/auth/register"
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        // Si es un endpoint público, no añadir token
        if (isPublicEndpoint(path)) {
            return chain.proceed(originalRequest)
        }

        // Recuperar el token del SessionManager
        val token = runBlocking {
            sessionManager.getAuthToken()
        }

        // Construir la petición con headers
        val requestBuilder = originalRequest.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")

        // Añadir token si existe
        if (!token.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val authenticatedRequest = requestBuilder.build()

        // Ejecutar la petición
        val response = chain.proceed(authenticatedRequest)

        // Manejar respuesta 401 (Unauthorized)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            handleUnauthorized()
        }

        return response
    }

    /**
     * Verifica si el endpoint es público (no requiere autenticación)
     */
    private fun isPublicEndpoint(path: String): Boolean {
        return PUBLIC_ENDPOINTS.any { publicPath ->
            path.contains(publicPath, ignoreCase = true)
        }
    }

    /**
     * Maneja respuestas 401 Unauthorized
     * Limpia la sesión para que el usuario tenga que volver a autenticarse
     */
    private fun handleUnauthorized() {
        runBlocking {
            sessionManager.clearSession()
        }
    }
}