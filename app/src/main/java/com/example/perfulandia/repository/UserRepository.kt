/*

package com.example.perfulandia.repository

import android.content.Context
import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.ApiService
import com.example.perfulandia.data.remote.RetrofitClient
import com.example.perfulandia.data.remote.dto.AuthResponse
import com.example.perfulandia.data.remote.dto.LoginRequest
import com.example.perfulandia.data.remote.dto.SignupRequest
import com.example.perfulandia.data.remote.dto.UserDto

/**
 * Repository: Abstrae la fuente de datos
 * El ViewModel NO sabe si los datos vienen de API, base de datos local, etc.
 */
class UserRepository(context: Context) {

    // 1. Crear la instancia del API Service (pasando el contexto)
    private val apiService: ApiService = RetrofitClient
        .create(context)
        .create(ApiService::class.java)

    // 2. Crear instancia del SessionManager para guardar el token y que haya persistencia
    private val sessionManager = SessionManager(context)

    /**
     * LOGIN - Autentica al usuario y guarda el token
     */
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            // 1. Llamar a la API
            val response: AuthResponse = apiService.login(LoginRequest(email, password))

            // 2. Guardar el token y user_id localmente
            sessionManager.saveSession(response.authToken, response.userId)

            // 3. Retornar éxito (sin necesidad de devolver datos)
            Result.success(Unit)
        } catch (e: Exception) {
            // Si algo falla (sin internet, credenciales inválidas, etc.)
            Result.failure(e)
        }
    }

    /**
     * REGISTRO - Crea un usuario y guarda el token
     */
    suspend fun signup(name: String, email: String, password: String): Result<Unit> {
        return try {
            // 1. Llamar a la API
            val response: AuthResponse = apiService.signup(SignupRequest(name, email, password))

            // 2. Guardar el token y user_id
            sessionManager.saveSession(response.authToken, response.userId)

            // 3. Retornar éxito
            Result.success(Unit)
        } catch (e: Exception) {
            // Manejar error
            Result.failure(e)
        }
    }

    /**
     * OBTENER USUARIO ACTUAL
     * Requiere que exista un token guardado en SessionManager
     */
    suspend fun getCurrentUser(): Result<UserDto> {
        return try {
            // 1. Llamar a la API (el AuthInterceptor añade el token)
            val user = apiService.getCurrentUser()

            // 2. Retornar éxito con los datos del usuario
            Result.success(user)
        } catch (e: Exception) {
            // Manejar error de red, token inválido, etc.
            Result.failure(e)
        }
    }
}
*/