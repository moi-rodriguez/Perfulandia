package com.example.perfulandia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        authRepository = mockk(relaxed = true)
        viewModel = LoginViewModel(authRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `el estado inicial es correcto`() = runTest {
        assertEquals(LoginUiState(), viewModel.uiState.value)
    }

    @Test
    fun `el login exitoso debe actualizar el uiState con el usuario y isSuccess`() = runTest {
        val user = User("1", "Diego", "diego@test.com", "user")
        coEvery { authRepository.login("diego@test.com", "password") } returns Result.success(user)

        viewModel.uiState.test {
            viewModel.login("diego@test.com", "password")

            assertEquals(LoginUiState(), awaitItem())
            assertEquals(LoginUiState(isLoading = true), awaitItem())
            
            val successState = awaitItem()
            assertEquals(user, successState.user)
            assertTrue(successState.isSuccess)
        }
    }

    @Test
    fun `el login fallido debe actualizar el uiState con error`() = runTest {
        val errorMessage = "Invalid credentials"
        coEvery { authRepository.login("diego@test.com", "wrong_password") } returns Result.failure(Exception(errorMessage))

        viewModel.uiState.test {
            viewModel.login("diego@test.com", "wrong_password")

            assertEquals(LoginUiState(), awaitItem())
            assertEquals(LoginUiState(isLoading = true), awaitItem())

            val errorState = awaitItem()
            assertEquals(errorMessage, errorState.error)
            assertFalse(errorState.isSuccess)
        }
    }

    @Test
    fun `el login con email vacio debe actualizar el uiState con error`() = runTest {
        val errorMessage = "El email no puede estar vacio"
        coEvery { authRepository.login("", "password") } returns Result.failure(IllegalArgumentException(errorMessage))

        viewModel.uiState.test {
            viewModel.login("", "password")

            assertEquals(LoginUiState(), awaitItem())
            assertEquals(LoginUiState(isLoading = true), awaitItem())

            val errorState = awaitItem()
            assertEquals(errorMessage, errorState.error)
            assertFalse(errorState.isSuccess)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `resetState debe volver al uiState inicial`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception("Error"))

        viewModel.uiState.test {
            assertEquals(LoginUiState(), awaitItem())

            viewModel.login("test", "test")
            assertEquals(LoginUiState(isLoading = true), awaitItem())
            assertNotNull(awaitItem().error)

            viewModel.resetState()
            assertEquals(LoginUiState(), awaitItem())
        }
    }
}
