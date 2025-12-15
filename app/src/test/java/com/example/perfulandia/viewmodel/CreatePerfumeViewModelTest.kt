package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.repository.CategoryRepository
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.model.Category
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class CreatePerfumeViewModelTest {

    private lateinit var viewModel: CreatePerfumeViewModel
    private lateinit var perfumeRepository: PerfumeRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var sessionManager: SessionManager

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        perfumeRepository = mockk(relaxed = true)
        categoryRepository = mockk()
        sessionManager = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init fetches categories successfully`() = runTest {
        val categories = listOf(Category("1", "Floral", null), Category("2", "Citrus", null))
        coEvery { categoryRepository.getAllCategories() } returns Result.success(categories)

        viewModel = CreatePerfumeViewModel(perfumeRepository, categoryRepository, sessionManager)

        val state = viewModel.uiState.value
        assertEquals(categories, state.categories)
        assertNull(state.error)
    }

    @Test
    fun `init fails to fetch categories`() = runTest {
        val errorMessage = "Error loading categories"
        coEvery { categoryRepository.getAllCategories() } returns Result.failure(Exception(errorMessage))

        viewModel = CreatePerfumeViewModel(perfumeRepository, categoryRepository, sessionManager)

        val state = viewModel.uiState.value
        assertEquals(0, state.categories.size)
        assertNotNull(state.error)
    }

    @Test
    fun `createPerfume success`() = runTest {
        coEvery { categoryRepository.getAllCategories() } returns Result.success(emptyList()) // Setup for init block
        viewModel = CreatePerfumeViewModel(perfumeRepository, categoryRepository, sessionManager)
        
        coEvery { perfumeRepository.createPerfume(any()) } returns Result.success(mockk())

        viewModel.createPerfume("Nombre", "Marca", "Fragancia", 100, "Unisex", 50.0, 10, "1", "Desc", "img")
        
        // With UnconfinedDispatcher, the final state should be asserted directly
        assertEquals(SubmissionStatus.SUCCESS, viewModel.uiState.value.submissionStatus)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `createPerfume failure`() = runTest {
        val errorMessage = "Creation failed"
        coEvery { categoryRepository.getAllCategories() } returns Result.success(emptyList()) // Setup for init block
        viewModel = CreatePerfumeViewModel(perfumeRepository, categoryRepository, sessionManager)
        
        coEvery { perfumeRepository.createPerfume(any()) } throws Exception(errorMessage)

        viewModel.createPerfume("Nombre", "Marca", "Fragancia", 100, "Unisex", 50.0, 10, "1", "Desc", "img")
        
        val state = viewModel.uiState.value
        assertEquals(SubmissionStatus.ERROR, state.submissionStatus)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `logout calls sessionManager clearSession`() = runTest {
        coEvery { categoryRepository.getAllCategories() } returns Result.success(emptyList())
        viewModel = CreatePerfumeViewModel(perfumeRepository, categoryRepository, sessionManager)
        
        viewModel.logout()

        coVerify(exactly = 1) { sessionManager.clearSession() }
    }
    
    @Test
    fun `resetSubmissionStatus works`() = runTest {
        coEvery { categoryRepository.getAllCategories() } returns Result.success(emptyList())
        viewModel = CreatePerfumeViewModel(perfumeRepository, categoryRepository, sessionManager)
        
        // Set an error state first
        coEvery { perfumeRepository.createPerfume(any()) } throws Exception("Error")
        viewModel.createPerfume("a", "b", "c", 1, "d", 1.0, 1, "1", "e", "f")
        assertEquals(SubmissionStatus.ERROR, viewModel.uiState.value.submissionStatus)
        
        // Now reset
        viewModel.resetSubmissionStatus()
        
        val state = viewModel.uiState.value
        assertEquals(SubmissionStatus.IDLE, state.submissionStatus)
        assertNull(state.error)
    }
}
