package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.repository.ReviewRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateReviewViewModelTest {

    private lateinit var viewModel: CreateReviewViewModel
    private lateinit var reviewRepository: ReviewRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        reviewRepository = mockk()
        viewModel = CreateReviewViewModel(reviewRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onRatingChange updates state`() {
        viewModel.onRatingChange(4)
        assertEquals(4, viewModel.uiState.value.rating)
    }

    @Test
    fun `onCommentChange updates state`() {
        val comment = "Excellent perfume!"
        viewModel.onCommentChange(comment)
        assertEquals(comment, viewModel.uiState.value.comment)
    }

    @Test
    fun `submitReview success`() = runTest {
        val perfumeId = "p1"
        val clienteId = "c1"
        val rating = 5
        val comment = "Loved it!"
        coEvery { reviewRepository.createReview(perfumeId, clienteId, rating, comment) } returns Result.success(Unit)

        viewModel.onRatingChange(rating)
        viewModel.onCommentChange(comment)
        viewModel.submitReview(perfumeId, clienteId)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isSuccess)
        assertNull(state.error)
    }

    @Test
    fun `submitReview failure`() = runTest {
        val perfumeId = "p1"
        val clienteId = "c1"
        val rating = 5
        val comment = "Loved it!"
        val errorMessage = "API Error"

        coEvery { reviewRepository.createReview(perfumeId, clienteId, rating, comment) } returns Result.failure(Exception(errorMessage))

        viewModel.onRatingChange(rating)
        viewModel.onCommentChange(comment)
        viewModel.submitReview(perfumeId, clienteId)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isSuccess)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `submitReview with zero rating fails validation`() = runTest {
        val perfumeId = "p1"
        val clienteId = "c1"

        viewModel.submitReview(perfumeId, clienteId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.error)
        assertEquals("Por favor, selecciona una calificación.", state.error)

        // Verify that the repository method was NOT called
        coVerify(exactly = 0) { reviewRepository.createReview(any(), any(), any(), any()) }
    }
}
