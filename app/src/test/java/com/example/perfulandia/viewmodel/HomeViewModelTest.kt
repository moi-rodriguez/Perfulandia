package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.repository.CategoryRepository
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.model.Category
import com.example.perfulandia.model.Perfume
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var perfumeRepository: PerfumeRepository
    private lateinit var categoryRepository: CategoryRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        perfumeRepository = mockk()
        categoryRepository = mockk()
        viewModel = HomeViewModel(perfumeRepository, categoryRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData success`() = runTest {
        val perfumes = listOf(Perfume(id = "1", nombre = "Perfume 1", categoriaId = "cat1"))
        val categories = listOf(Category(id = "cat1", nombre = "Category 1"))

        coEvery { perfumeRepository.getAllPerfumes() } returns Result.success(perfumes)
        coEvery { categoryRepository.getAllCategories() } returns Result.success(categories)

        viewModel.loadData()

        assertTrue(viewModel.uiState.value.isLoading)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(perfumes, state.perfumes)
        assertEquals(perfumes, state.allPerfumes)
        assertEquals(listOf(Category(id = "Todos", nombre = "Todos")) + categories, state.categories)
        assertEquals(null, state.error)
    }

    @Test
    fun `loadData perfume failure`() = runTest {
        val errorMessage = "Error fetching perfumes"
        coEvery { perfumeRepository.getAllPerfumes() } returns Result.failure(Exception(errorMessage))
        coEvery { categoryRepository.getAllCategories() } returns Result.success(emptyList())

        viewModel.loadData()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("perfumes"))
        assertTrue(state.perfumes.isEmpty())
        assertTrue(state.allPerfumes.isEmpty())
    }

    @Test
    fun `loadData category failure`() = runTest {
        val errorMessage = "Error fetching categories"
        coEvery { perfumeRepository.getAllPerfumes() } returns Result.success(emptyList())
        coEvery { categoryRepository.getAllCategories() } returns Result.failure(Exception(errorMessage))

        viewModel.loadData()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("categorías"))
        assertEquals(listOf(Category(id = "Todos", nombre = "Todos")), state.categories)
    }

    @Test
    fun `filterByCategory filters perfumes`() = runTest {
        val perfumes = listOf(
            Perfume(id = "1", nombre = "Chanel No. 5", categoriaId = "cat1"),
            Perfume(id = "2", nombre = "Dior Sauvage", categoriaId = "cat2"),
            Perfume(id = "3", nombre = "Bleu de Chanel", categoriaId = "cat1")
        )
        val categories = listOf(
            Category(id = "cat1", nombre = "Floral"),
            Category(id = "cat2", nombre = "Amaderado")
        )
        coEvery { perfumeRepository.getAllPerfumes() } returns Result.success(perfumes)
        coEvery { categoryRepository.getAllCategories() } returns Result.success(categories)

        viewModel.loadData()
        advanceUntilIdle()

        viewModel.filterByCategory("Floral")
        var state = viewModel.uiState.value
        assertEquals("Floral", state.selectedCategory)
        assertEquals(2, state.perfumes.size)
        assertTrue(state.perfumes.all { it.categoriaId == "cat1" })

        viewModel.filterByCategory("Amaderado")
        state = viewModel.uiState.value
        assertEquals("Amaderado", state.selectedCategory)
        assertEquals(1, state.perfumes.size)
        assertTrue(state.perfumes.all { it.categoriaId == "cat2" })
    }

    @Test
    fun `filterByCategory with 'Todos' resets filter`() = runTest {
        val perfumes = listOf(
            Perfume(id = "1", nombre = "Chanel No. 5", categoriaId = "cat1"),
            Perfume(id = "2", nombre = "Dior Sauvage", categoriaId = "cat2")
        )
        val categories = listOf(Category(id = "cat1", nombre = "Floral"))
        coEvery { perfumeRepository.getAllPerfumes() } returns Result.success(perfumes)
        coEvery { categoryRepository.getAllCategories() } returns Result.success(categories)

        viewModel.loadData()
        advanceUntilIdle()

        viewModel.filterByCategory("Floral")
        var state = viewModel.uiState.value
        assertEquals(1, state.perfumes.size)

        viewModel.filterByCategory("Todos")
        state = viewModel.uiState.value
        assertEquals("Todos", state.selectedCategory)
        assertEquals(perfumes.size, state.perfumes.size)
        assertEquals(perfumes, state.perfumes)
    }
}