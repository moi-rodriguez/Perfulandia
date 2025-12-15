package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.local.SessionManager
import com.example.perfulandia.data.repository.PerfumeRepository
import com.example.perfulandia.data.repository.ReviewRepository
import com.example.perfulandia.model.Perfume
import com.example.perfulandia.model.Review
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PerfumeDetailViewModelTest {

    private lateinit var viewModel: PerfumeDetailViewModel
    private lateinit var perfumeRepository: PerfumeRepository
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var cartViewModel: CartViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        perfumeRepository = mockk()
        reviewRepository = mockk()
        sessionManager = mockk(relaxed = true)
        cartViewModel = mockk(relaxed = true)
        viewModel = PerfumeDetailViewModel(perfumeRepository, reviewRepository, sessionManager, cartViewModel)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPerfume success`() = runTest {
        val perfume = Perfume("1", "Perfume Name", "Brand", "Fragrance", 100, "Unisex", 99.99, 10, "1", "catNombre", "Description", "image_url", "thumb_url")
        val reviews = listOf(Review("r1", 5, "Great!", "user"))
        coEvery { perfumeRepository.getPerfumeById("1") } returns Result.success(perfume)
        coEvery { reviewRepository.getReviewsByPerfumeId("1") } returns Result.success(reviews)

        viewModel.loadPerfume("1")

        val state = viewModel.uiState.value
        assertEquals(perfume, state.perfume)
        assertEquals(reviews, state.reviews)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `loadPerfume failure`() = runTest {
        val errorMessage = "Perfume not found"
        coEvery { perfumeRepository.getPerfumeById("1") } returns Result.failure(Exception(errorMessage))

        viewModel.loadPerfume("1")

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.perfume)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains(errorMessage))
    }

    @Test
    fun `loadPerfume success but reviews failure`() = runTest {
        val perfume = Perfume("1", "Perfume Name", "Brand", "Fragrance", 100, "Unisex", 99.99, 10, "1", "catNombre", "Description", "image_url", "thumb_url")
        coEvery { perfumeRepository.getPerfumeById("1") } returns Result.success(perfume)
        coEvery { reviewRepository.getReviewsByPerfumeId("1") } returns Result.failure(Exception("Error loading reviews"))

        viewModel.loadPerfume("1")

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(perfume, state.perfume)
        assertTrue(state.reviews.isEmpty())
        assertNull(state.error)
    }

    @Test
    fun `onAddToCartClicked as guest`() = runTest {
        coEvery { sessionManager.getUserEmail() } returns "invitado@sistema.com"

        viewModel.onAddToCartClicked()

        val state = viewModel.uiState.value
        assertTrue(state.showLoginRequiredMessage)
        coVerify { sessionManager.clearSession() }
    }

    @Test
    fun `onAddToCartClicked as logged-in user`() = runTest {
        val perfume = Perfume("1", "Perfume Name", "Brand", "Fragrance", 100, "Unisex", 99.99, 10, "1", "catNombre", "Description", "image_url", "thumb_url")
        coEvery { perfumeRepository.getPerfumeById("1") } returns Result.success(perfume)
        coEvery { reviewRepository.getReviewsByPerfumeId(any()) } returns Result.success(emptyList())
        coEvery { sessionManager.getUserEmail() } returns "cliente@test.com"

        viewModel.loadPerfume("1")

        viewModel.onAddToCartClicked()

        val state = viewModel.uiState.value
        assertTrue(state.showAddedToCartMessage)
        coVerify { cartViewModel.addToCart(perfume) }
    }
}
