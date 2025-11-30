package com.example.perfulandia.data.repository

import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.remote.api.AuthApi
import com.example.perfulandia.data.remote.dto.AuthResponse
import com.example.perfulandia.data.remote.dto.UserDto
import com.example.perfulandia.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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

    private val testUserDto = UserDto("1", "Test User", "test@example.com", "user")
    private val testUser = User("1", "Test User", "test@example.com", "user")
    private val testToken = "test_token"

    @Before
    fun setUp() {
        authApi = mockk()
        sessionManager = mockk(relaxed = true)
        authRepository = AuthRepository(authApi, sessionManager)
    }

    @Test
    fun `el login exitoso debe guardar la sesion y devolver el usuario`() = runTest {
        // Given
        val loginResponse = AuthResponse(success = true, token = testToken, user = testUserDto, message = "Login successful")
        coEvery { authApi.login(any()) } returns Response.success(loginResponse)
        coEvery { sessionManager.saveSession(any(), any(), any(), any(), any()) } just runs

        // When
        val result = authRepository.login("test@example.com", "password")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(testUser, result.getOrNull())
        coVerify {
            sessionManager.saveSession(
                token = testToken,
                userId = testUserDto._id,
                userName = testUserDto.nombre,
                userEmail = testUserDto.email,
                userRole = testUserDto.role
            )
        }
    }

    @Test
    fun `el login fallido con 401 debe devolver un fallo con un mensaje especifico`() = runTest {
        // Given
        coEvery { authApi.login(any()) } returns Response.error(401, "".toResponseBody())

        // When
        val result = authRepository.login("test@example.com", "wrong_password")

        // Then
        assertTrue(result.isFailure)
        assertEquals("Credenciales invalidas", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { sessionManager.saveSession(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `el login con error de conexion debe devolver un fallo`() = runTest {
        // Given
        val exception = Exception("Connection failed")
        coEvery { authApi.login(any()) } throws exception

        // When
        val result = authRepository.login("test@example.com", "password")

        // Then
        assertTrue(result.isFailure)
        assertEquals("Error de conexion: ${exception.message}", result.exceptionOrNull()?.message)
    }

    @Test
    fun `el registro exitoso debe guardar la sesion y devolver el usuario`() = runTest {
        // Given
        val registerResponse = AuthResponse(success = true, token = testToken, user = testUserDto, message = "Register successful")
        coEvery { authApi.register(any()) } returns Response.success(registerResponse)
        coEvery { sessionManager.saveSession(any(), any(), any(), any(), any()) } just runs

        // When
        val result = authRepository.register("Test User", "test@example.com", "password")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(testUser, result.getOrNull())
        coVerify {
            sessionManager.saveSession(
                token = testToken,
                userId = testUserDto._id,
                userName = testUserDto.nombre,
                userEmail = testUserDto.email,
                userRole = testUserDto.role
            )
        }
    }

    @Test
    fun `el registro fallido con 409 debe devolver un fallo con un mensaje especifico`() = runTest {
        // Given
        coEvery { authApi.register(any()) } returns Response.error(409, "".toResponseBody())

        // When
        val result = authRepository.register("Test User", "test@example.com", "password")

        // Then
        assertTrue(result.isFailure)
        assertEquals("El email ya esta registrado", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { sessionManager.saveSession(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `el logout debe limpiar la sesion`() = runTest {
        // Given
        coEvery { sessionManager.clearSession() } just runs

        // When
        authRepository.logout()

        // Then
        coVerify { sessionManager.clearSession() }
    }

    @Test
    fun `isLoggedIn debe devolver el valor de sessionManager`() = runTest {
        // Given
        coEvery { sessionManager.isLoggedIn() } returns true

        // When
        val isLoggedIn = authRepository.isLoggedIn()

        // Then
        assertTrue(isLoggedIn)
    }
}
