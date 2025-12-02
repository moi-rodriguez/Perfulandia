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
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: RegisterViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        authRepository = mockk(relaxed = true) // Usamos relaxed para no tener que definir el `coEvery` en cada test
        viewModel = RegisterViewModel(authRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `el registro exitoso debe actualizar el uiState con el usuario y isSuccess`() = runTest {
        val user = User("1", "Diego", "diego@test.com", "user")
        coEvery { authRepository.register("Diego", "diego@test.com", "password") } returns Result.success(user)

        viewModel.uiState.test {
            viewModel.register("Diego", "diego@test.com", "password")

            // Initial state
            assertEquals(SignupUiState(isLoading = false), awaitItem())
            // Loading state
            assertEquals(SignupUiState(isLoading = true), awaitItem())
            // Success state
            val successState = awaitItem()
            assertEquals(user, successState.user)
            assertTrue(successState.isSuccess)
            assertFalse(successState.isLoading)
            assertNull(successState.error)
        }
    }

    @Test
    fun `el registro fallido debe actualizar el uiState con un error`() = runTest {
        val errorMessage = "Correo invalido."
        coEvery { authRepository.register(any(), any(), any()) } returns Result.failure(Exception(errorMessage))

        viewModel.uiState.test {
            viewModel.register("Test User", "test@test.com", "password")

            // Initial state
            assertEquals(SignupUiState(isLoading = false), awaitItem())
            // Loading state
            assertEquals(SignupUiState(isLoading = true), awaitItem())
            // Error state
            val errorState = awaitItem()
            assertNull(errorState.user)
            assertFalse(errorState.isSuccess)
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
        }
    }

    @Test
    fun `el registro fallido por contraseña invalida debe actualizar el uiState con un error`() = runTest {
        val errorMessage = "Contraseña Invalida."
        coEvery { authRepository.register(any(), any(), any()) } returns Result.failure(Exception(errorMessage))

        viewModel.uiState.test {
            viewModel.register("Test User", "test@test.com", "invalidpassword")

            // Initial state
            assertEquals(SignupUiState(isLoading = false), awaitItem())
            // Loading state
            assertEquals(SignupUiState(isLoading = true), awaitItem())
            // Error state
            val errorState = awaitItem()
            assertNull(errorState.user)
            assertFalse(errorState.isSuccess)
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
        }
    }
}