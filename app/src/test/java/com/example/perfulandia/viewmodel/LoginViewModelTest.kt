package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success`() = runTest {
        val user = User("1", "Test", "test@test.com", "CLIENTE")
        coEvery { authRepository.login(any(), any()) } returns Result.success(user)

        viewModel.login("test@test.com", "123456")

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(user, state.user)
        assertNull(state.error)
        assert(state.isSuccess)
    }

    @Test
    fun `login failure`() = runTest {
        val errorMessage = "Invalid credentials"
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception(errorMessage))

        viewModel.login("test@test.com", "wrongpassword")

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.user)
        assertEquals(errorMessage, state.error)
        assert(!state.isSuccess)
    }

    @Test
    fun `invalid email format`() = runTest {
        viewModel.login("invalid-email", "123456")

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.emailError)
        assertNull(state.passwordError)
        assertNull(state.error)
        assert(!state.isSuccess)
    }

    @Test
    fun `password too short`() = runTest {
        viewModel.login("test@test.com", "123")

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.emailError)
        assertNotNull(state.passwordError)
        assertNull(state.error)
        assert(!state.isSuccess)
    }

    @Test
    fun `multiple validation errors`() = runTest {
        viewModel.login("invalid-email", "123")

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.emailError)
        assertNotNull(state.passwordError)
        assertNull(state.error)
        assert(!state.isSuccess)
    }
}
