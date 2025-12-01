package com.example.perfulandia.data.repository

import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.mapper.UserMapper
import com.example.perfulandia.model.User
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.dto.LoginRequest
import com.example.perfulandia.data.remote.dto.RegisterRequest

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
    private val sessionManager: SessionManager,
    private val userMapper: UserMapper = UserMapper
) {

    /**
     * Login del usuario
     * @return Result con UserDto si es exitoso, o Exception si falla
     */
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val request = LoginRequest(email, password)
            val response = authApi.login(request)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success && body.data != null) {
                    val authData = body.data // Accedemos al objeto intermedio

                    sessionManager.saveSession(
                        token = authData.token, // Sacamos el token de data
                        userId = authData.user._id,
                        // Protección: Usamos el nombre del formulario si el del server viene null
                        userName = authData.user.nombre ?: "Usuario",
                        userEmail = authData.user.email,
                        userRole = authData.user.role
                    )

                    // Retornamos el usuario mapeado
                    Result.success(UserMapper.fromDto(authData.user))
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
    suspend fun register(nombre: String, email: String, password: String): Result<User> {
        return try {
            val request = RegisterRequest(
                nombre = nombre,
                email = email,
                password = password,
                role = "CLIENTE",
                telefono = "000000000",
                direccion = "Sin dirección",
                preferencias = listOf("Ninguna")
            )

            val response = authApi.register(request)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success && body.data != null) {
                    val authData = body.data

                    // Lógica inteligente: Si el servidor no devuelve nombre, usamos el del formulario
                    val nombreFinal = authData.user.nombre ?: nombre

                    sessionManager.saveSession(
                        token = authData.token,
                        userId = authData.user._id,
                        userName = nombreFinal, // Guardamos el nombre correcto
                        userEmail = authData.user.email,
                        userRole = authData.user.role
                    )

                    // Creamos el objeto Usuario manualmente para devolverlo con el nombre correcto
                    // (En lugar de usar UserMapper.fromDto(authData.user) que pondría "Usuario")
                    val userDomain = User(
                        id = authData.user._id,
                        nombre = nombreFinal,
                        email = authData.user.email,
                        role = authData.user.role,
                        createdAt = authData.user.createdAt
                    )

                    Result.success(userDomain)
                } else {
                    Result.failure(Exception(body?.message ?: "Registro fallido o respuesta vacía"))
                }
            } else {
                // ... (el manejo de errores que ya tenías) ...
                val errorMessage = when (response.code()) {
                    409 -> "El email ya está registrado"
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
    suspend fun getProfile(): Result<User> {
        return try {
            val response = authApi.getProfile()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success) {
                    Result.success(UserMapper.fromDto(body.user))
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
    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val response = authApi.getUsers()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success) {
                    Result.success(UserMapper.fromDtoList(body.data))
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