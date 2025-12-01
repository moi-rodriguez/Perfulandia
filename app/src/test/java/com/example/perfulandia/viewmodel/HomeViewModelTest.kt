package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.repository.CategoriaRepository
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.model.Categoria
import com.example.perfulandia.model.Perfume
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var perfumeRepository: PerfumeRepository
    private lateinit var categoriaRepository: CategoriaRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        perfumeRepository = mockk()
        categoriaRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al iniciar carga perfumes y categorias correctamente`() = runTest {
        // GIVEN
        val perfumesMock = listOf(Perfume("1", "Perfume A"))
        val categoriasMock = listOf(Categoria("1", "Floral"))

        coEvery { perfumeRepository.getAllPerfumes() } returns Result.success(perfumesMock)
        coEvery { categoriaRepository.getAllCategorias() } returns Result.success(categoriasMock)

        // WHEN (Inicializamos el ViewModel)
        viewModel = HomeViewModel(perfumeRepository, categoriaRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        val state = viewModel.uiState.value
        assertEquals(perfumesMock, state.allPerfumes)
        assertEquals(categoriasMock, state.categories)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `cuando cargar perfumes falla el estado muestra error`() = runTest {
        // GIVEN: El repositorio devuelve un error
        val errorMessage = "Error de conexión"
        coEvery { perfumeRepository.getAllPerfumes() } returns Result.failure(Exception(errorMessage))
        coEvery { categoriaRepository.getAllCategorias() } returns Result.success(emptyList())

        // WHEN: Se inicializa el ViewModel, lo que dispara la carga de datos en su bloque init
        viewModel = HomeViewModel(perfumeRepository, categoriaRepository)
        testDispatcher.scheduler.advanceUntilIdle() // Asegura que las corutinas de init() terminen

        // THEN: El estado de la UI debe reflejar el error
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        // Verificamos que el mensaje de error en el estado contenga el texto esperado
        assert(state.error?.contains(errorMessage) == true)
    }
}