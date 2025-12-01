package com.example.perfulandia.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.perfulandia.data.repository.AuthRepository
import com.example.perfulandia.data.repository.AvatarRepository
import com.example.perfulandia.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    // Regla para ejecutar tareas de fondo de los Componentes de Arquitectura de forma síncrona.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Mocks para las dependencias del ViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var avatarRepository: AvatarRepository

    // El ViewModel que estamos probando
    private lateinit var viewModel: ProfileViewModel

    // Dispatcher para controlar las corrutinas en las pruebas
    private val testDispatcher = StandardTestDispatcher()

    // Datos de prueba
    private val testUser = User(id = "1", nombre = "Test User", email = "test@example.com", role = "user")
    private val testAvatarUri = mockk<Uri>()

    @Before
    fun setUp() {
        // Configurar el dispatcher principal para las pruebas
        Dispatchers.setMain(testDispatcher)

        // Inicializar los mocks
        authRepository = mockk(relaxUnitFun = true) // Usar relaxUnitFun para ignorar llamadas a funciones Unit
        avatarRepository = mockk()

        // --- INICIO DE LA SOLUCIÓN ---
        // Se mueve la configuración específica de los mocks a cada test individual
        // para evitar conflictos y asegurar un estado predecible.
        // --- FIN DE LA SOLUCIÓN ---
    }

    @After
    fun tearDown() {
        // Limpiar el dispatcher principal después de cada prueba
        Dispatchers.resetMain()
    }

    @Test
    fun `el perfil del usuario y el avatar se cargan correctamente durante la inicializacion`() = runTest(testDispatcher) {
        // --- INICIO DE LA SOLUCIÓN ---
        // Dado: configurar los mocks para el caso de éxito
        coEvery { avatarRepository.getAvatarUri() } returns flowOf(testAvatarUri)
        coEvery { authRepository.getProfile() } returns Result.success(testUser)
        coEvery { authRepository.getUserName() } returns "Test User"
        // --- FIN DE LA SOLUCIÓN ---

        // Cuando se inicializa el ViewModel
        viewModel = ProfileViewModel(authRepository, avatarRepository)

        // Avanzar la ejecución de corrutinas hasta que estén inactivas
        advanceUntilIdle()

        // Entonces el estado de la UI debe reflejar los datos cargados
        val currentState = viewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertEquals(testUser, currentState.user)
        assertEquals(testAvatarUri, currentState.avatarUri)
        assertNull(currentState.error)
    }
}
