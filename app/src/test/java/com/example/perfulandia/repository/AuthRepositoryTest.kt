package com.example.perfulandia.repository

import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.dto.AuthData
import com.example.perfulandia.data.remote.dto.AuthResponse
import com.example.perfulandia.data.remote.dto.LoginRequest
import com.example.perfulandia.data.remote.dto.UserDto
import com.example.perfulandia.data.repository.AuthRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    // Mocks
    private lateinit var mockAuthApi: AuthApi
    private lateinit var mockSessionManager: SessionManager

    // SUT (System Under Test)
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        // Crear mocks
        mockAuthApi = mockk()
        mockSessionManager = mockk()

        // Configurar comportamiento de SessionManager
        coEvery { mockSessionManager.saveSession(any(), any(), any(), any(), any()) } just Runs
        coEvery { mockSessionManager.clearSession() } just Runs

        // Crear instancia del repository
        repository = AuthRepository(mockAuthApi, mockSessionManager)
    }

    @After
    fun teardown() {
        // Limpiar todos los mocks después de cada prueba
        unmockkAll()
    }

    @Test
    fun `login exitoso debe guardar la sesion y retornar Success con User`() = runTest {
        // Given - Preparar datos de prueba
        val email = "test@example.com"
        val password = "password"
        val token = "test_token_jwt"

        val userDto = UserDto(
            _id = "1",
            nombre = "Test User",
            email = email,
            role = "CLIENTE"
        )

        val authData = AuthData(user = userDto, token = token)
        val apiResponse = AuthResponse(success = true, data = authData, message = "Login OK")
        val response = Response.success(apiResponse)

        // Configurar mock para retornar respuesta exitosa
        coEvery { mockAuthApi.login(LoginRequest(email, password)) } returns response

        // When - Ejecutar login
        val result = repository.login(email, password)

        // Then - Verificar resultado
        assertTrue("El resultado debería ser Success", result.isSuccess)

        val user = result.getOrNull()
        assertEquals("1", user?.id)
        assertEquals("Test User", user?.nombre)
        assertEquals(email, user?.email)
        assertEquals("CLIENTE", user?.role)

        // Verificar que se llamó a guardar sesión con los datos correctos
        coVerify {
            mockSessionManager.saveSession(
                token = token,
                userId = userDto._id,
                userName = userDto.nombre!!,
                userEmail = userDto.email,
                userRole = userDto.role
            )
        }
    }

    @Test
    fun `registro exitoso debe retornar Success con User`() = runTest {
        // Given
        val nombre = "Nuevo Usuario"
        val email = "nuevo@example.com"
        val password = "password123"

        val newUserDto = UserDto(
            _id = "newuser123",
            nombre = nombre,
            email = email,
            role = "CLIENTE"
        )
        val newToken = "new_token_12345"

        val authData = AuthData(
            user = newUserDto,
            token = newToken
        )

        val apiResponse = AuthResponse(
            success = true,
            data = authData
        )
        val response = Response.success(apiResponse)

        // Configurar mock
        coEvery { mockAuthApi.register(any()) } returns response

        // When
        val result = repository.register(
            nombre = nombre,
            email = email,
            password = password
        )

        // Then
        assertTrue(result.isSuccess)
        val user = result.getOrNull()
        assertEquals(nombre, user?.nombre)
        assertEquals(email, user?.email)
        assertEquals("CLIENTE", user?.role)

        // Verificar que se guardó la sesión
        coVerify {
            mockSessionManager.saveSession(
                token = newToken,
                userId = newUserDto._id,
                userName = newUserDto.nombre!!,
                userEmail = newUserDto.email,
                userRole = newUserDto.role
            )
        }
    }

    @Test
    fun `login con credenciales incorrectas debe retornar Failure`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrongpassword"
        val errorMessage = "Credenciales inválidas"

        val apiResponse = AuthResponse(success = false, message = errorMessage, data = null)
        val response = Response.success(apiResponse)

        // Configurar mock para retornar respuesta de fallo
        coEvery { mockAuthApi.login(LoginRequest(email, password)) } returns response

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue("El resultado debería ser Failure", result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)

        // Verificar que NO se llamó a guardar sesión
        coVerify(exactly = 0) {
            mockSessionManager.saveSession(any(), any(), any(), any(), any())
        }
    }
}
