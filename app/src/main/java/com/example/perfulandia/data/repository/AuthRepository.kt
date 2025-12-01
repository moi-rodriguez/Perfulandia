package com.example.perfulandia.data.repository

import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.mapper.UserMapper
import com.example.perfulandia.model.User
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.dto.LoginRequest
import com.example.perfulandia.data.remote.dto.RegisterRequest

class AuthRepository(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager,
    private val userMapper: UserMapper = UserMapper
) {

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val request = LoginRequest(email, password)
            val response = authApi.login(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    val authData = body.data
                    sessionManager.saveSession(
                        token = authData.token,
                        userId = authData.user._id,
                        userName = authData.user.nombre ?: "Usuario",
                        userEmail = authData.user.email,
                        userRole = authData.user.role
                    )
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
                    sessionManager.saveSession(
                        token = authData.token,
                        userId = authData.user._id,
                        userName = authData.user.nombre ?: nombre,
                        userEmail = authData.user.email,
                        userRole = authData.user.role
                    )
                    Result.success(UserMapper.fromDto(authData.user))
                } else {
                    Result.failure(Exception(body?.message ?: "Registro fallido"))
                }
            } else {
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

    suspend fun getProfile(): Result<User> {
        return try {
            val response = authApi.getProfile()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.success) {
                    // CORRECCIÓN: Obtenemos el DTO directamente de 'data'
                    val userDto = body.data

                    if (userDto != null) {
                        Result.success(UserMapper.fromDto(userDto))
                    } else {
                        // Si 'data' viene nulo aunque success sea true
                        Result.failure(Exception("Error al obtener perfil: Datos de usuario vacíos"))
                    }
                } else {
                    Result.failure(Exception("Error al obtener perfil: Respuesta vacía o success=false"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

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

    suspend fun logout() = sessionManager.clearSession()
    suspend fun isLoggedIn() = sessionManager.isLoggedIn()
    suspend fun isAdmin() = sessionManager.isAdmin()
    suspend fun getUserName() = sessionManager.getUserName()
    suspend fun getUserEmail() = sessionManager.getUserEmail()
}