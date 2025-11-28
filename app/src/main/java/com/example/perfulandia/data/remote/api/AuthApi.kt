package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.AuthResponse
import com.example.perfulandia.data.remote.dto.LoginRequest
import com.example.perfulandia.data.remote.dto.RegisterRequest
import com.example.perfulandia.data.remote.dto.ProfileResponse
import com.example.perfulandia.data.remote.dto.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * AuhtApi: Endpoints de autenticación
 * Base URL: /auth
 */
interface AuthApi {
    /**
     * POST /auth/login
     * Inicar sesión
     *
     * @param request Credenciales (email, password)
     * @return Token JWT + datos del usuario
     *
     */

    @POST("auth/login")
    suspend fun Login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    /**
     * POST /auth/register
     * Registrar nuevo usuario
     *
     * @param request Datos del usuario (nombre, email, password, role)
     * @return Token JWT + datos del usuario creado
     */

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /**
     * GET /auth/profile
     * Obtener perfil del usuario actual
     * Requiere: Token JWT en header Authorization
     *
     * @return Datos del usuario actual
     */
    @GET("auth/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    /**
     * GET /auth/users
     * Obtener lista de usuarios (solo admin)
     * Requiere: Token JWT en header Authorization + role: admin
     *
     *@return Lista de todos los usuarios
     */
    @GET("auth/users")
    suspend fun getUsers(): Response<UsersResponse>
}




