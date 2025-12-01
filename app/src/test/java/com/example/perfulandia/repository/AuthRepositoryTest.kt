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
        assertEquals("Credenciales inválidas", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { sessionManager.saveSession(any(), any(), any(), any(), any()) }
    }
}
