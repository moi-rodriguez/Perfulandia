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
}
