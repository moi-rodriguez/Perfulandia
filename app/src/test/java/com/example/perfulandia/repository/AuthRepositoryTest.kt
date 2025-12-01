package com.example.perfulandia.repository

import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.dto.AuthData
import com.example.perfulandia.data.remote.dto.AuthResponse
import com.example.perfulandia.data.remote.dto.UserDto
import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    private lateinit var authApi: AuthApi
    private lateinit var sessionManager: SessionManager
    private lateinit var authRepository: AuthRepository

    // Datos de prueba
    private val testUserDto = UserDto(
        _id = "1",
        nombre = "Test User",
        email = "test@example.com",
        role = "CLIENTE"
    )

    // El usuario esperado (Dominio)
    private val testUser = User(
        id = "1",
        nombre = "Test User",
        email = "test@example.com",
        role = "CLIENTE"
    )

    private val testToken = "test_token_jwt"

    @Before
    fun setUp() {
        authApi = mockk()
        sessionManager = mockk(relaxed = true) // relaxed = true permite llamar métodos sin configurar su respuesta explícita
        authRepository = AuthRepository(authApi, sessionManager)
    }

    @Test
    fun `el login exitoso debe guardar la sesion y devolver el usuario`() = runTest {
        // GIVEN (DADO)
        // 1. Preparamos la estructura anidada que espera el repositorio: Response -> AuthResponse -> AuthData -> UserDto
        val authData = AuthData(user = testUserDto, token = testToken)
        val loginResponse = AuthResponse(success = true, data = authData, message = "Login OK")

        // 2. Simulamos que la API devuelve éxito (200 OK) con esos datos
        coEvery { authApi.login(any()) } returns Response.success(loginResponse)

        // WHEN (CUANDO)
        val result = authRepository.login("test@example.com", "password")

        // THEN (ENTONCES)
        // 1. Verificamos que el resultado sea exitoso
        assertTrue(result.isSuccess)
        assertEquals(testUser, result.getOrNull())

        // 2. Verificamos que se haya llamado a guardar sesión con los datos correctos
        coVerify {
            sessionManager.saveSession(
                token = testToken,
                userId = testUserDto._id,
                userName = testUserDto.nombre!!, // Asumimos que no es null en el test
                userEmail = testUserDto.email,
                userRole = testUserDto.role
            )
        }
    }

    @Test
    fun `el login fallido con 401 debe devolver un fallo con un mensaje especifico`() = runTest {
        // GIVEN (DADO)
        // Simulamos un error 401 Unauthorized desde el servidor
        val errorBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { authApi.login(any()) } returns Response.error(401, errorBody)

        // WHEN (CUANDO)
        val result = authRepository.login("test@example.com", "wrong_password")

        // THEN (ENTONCES)
        // 1. Verificamos que sea un fallo
        assertTrue(result.isFailure)

        // 2. Verificamos el mensaje específico que definimos en el repositorio
        assertEquals("Credenciales inválidas", result.exceptionOrNull()?.message)

        // 3. Verificamos que NUNCA se intentó guardar sesión
        coVerify(exactly = 0) { sessionManager.saveSession(any(), any(), any(), any(), any()) }
    }
}