package com.example.perfulandia.data.repository

import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.dto.LoginRequest
import com.example.perfulandia.data.remote.dto.RegisterRequest
import com.example.perfulandia.data.remote.dto.UserDto

/**
 * AuthRepository: Maneja la autenticación y usuarios
 *
 * Responsabilidades:
 * - Login y registro de usuarios
 * - Gestión de sesión (guardar/limpiar token)
 * - Obtener perfil y lista de usuarios
 */
class AuthRepository(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) {

    /**
     * Login del usuario
     * @return Result con UserDto si es exitoso, o Exception si falla
     */
    suspend fun login(email: String, password: String): Result<UserDto> {
        return try {
            val request = LoginRequest(email, password)
            val response = authApi.login(request)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success) {
                    // Guardar sesión completa
                    sessionManager.saveSession(
                        token = body.token,
                        userId = body.user._id,
                        userName = body.user.nombre,
                        userEmail = body.user.email,
                        userRole = body.user.role
                    )

                    Result.success(body.user)
                } else {
                    Result.failure(Exception(body?.message ?: "Login fallido"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Credenciales inválidas"
                    404 -> "Usuario no encontrado"
                    500 -> "Error del servidor"
                    else -> "Error: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Registro de nuevo usuario
     */
    suspend fun register(
        nombre: String,
        email: String,
        password: String,
        role: String = "user"
    ): Result<UserDto> {
        return try {
            val request = RegisterRequest(nombre, email, password, role)
            val response = authApi.register(request)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success) {
                    // Guardar sesión automáticamente después del registro
                    sessionManager.saveSession(
                        token = body.token,
                        userId = body.user._id,
                        userName = body.user.nombre,
                        userEmail = body.user.email,
                        userRole = body.user.role
                    )

                    Result.success(body.user)
                } else {
                    Result.failure(Exception(body?.message ?: "Registro fallido"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    409 -> "El email ya está registrado"
                    400 -> "Datos inválidos"
                    500 -> "Error del servidor"
                    else -> "Error: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Obtener perfil del usuario actual
     */
    suspend fun getProfile(): Result<UserDto> {
        return try {
            val response = authApi.getProfile()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success) {
                    Result.success(body.user)
                } else {
                    Result.failure(Exception("Error al obtener perfil"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Obtener lista de usuarios (solo admin)
     */
    suspend fun getAllUsers(): Result<List<UserDto>> {
        return try {
            val response = authApi.getUsers()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception("Error al obtener usuarios"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    403 -> "No tienes permisos de administrador"
                    401 -> "No estás autenticado"
                    else -> "Error: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Cerrar sesión
     */
    suspend fun logout() {
        sessionManager.clearSession()
    }

    /**
     * Verificar si el usuario está autenticado
     */
    suspend fun isLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    /**
     * Verificar si el usuario actual es administrador
     */
    suspend fun isAdmin(): Boolean {
        return sessionManager.isAdmin()
    }

    /**
     * Obtener el nombre del usuario actual
     */
    suspend fun getUserName(): String? {
        return sessionManager.getUserName()
    }

    /**
     * Obtener el email del usuario actual
     */
    suspend fun getUserEmail(): String? {
        return sessionManager.getUserEmail()
    }
}