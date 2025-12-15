package com.example.perfulandia.repository

import com.example.perfulandia.data.remote.api.ReviewApi
import com.example.perfulandia.data.remote.api.ReviewListResponse
import com.example.perfulandia.data.remote.api.SingleReviewResponse
import com.example.perfulandia.data.remote.dto.ReviewDto
import com.example.perfulandia.data.repository.ReviewRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class ReviewRepositoryTest {

    private lateinit var repository: ReviewRepository
    private lateinit var api: ReviewApi

    @Before
    fun setUp() {
        api = mockk()
        repository = ReviewRepository(api)
    }

    //region getReviewsByPerfumeId Tests
    @Test
    fun `getReviewsByPerfumeId success`() = runTest {
        val reviewDtos = listOf(
            ReviewDto("p1", "c1", 5, "Excellent!"),
            ReviewDto("p1", "c2", 4, "Very good")
        )
        val apiResponse = ReviewListResponse(success = true, data = reviewDtos, message = null)
        coEvery { api.getReviewsByPerfumeId("p1") } returns Response.success(apiResponse)

        val result = repository.getReviewsByPerfumeId("p1")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getReviewsByPerfumeId api error`() = runTest {
        val apiResponse = ReviewListResponse(success = false, data = emptyList(), message = "API error")
        coEvery { api.getReviewsByPerfumeId("p1") } returns Response.success(apiResponse)

        val result = repository.getReviewsByPerfumeId("p1")

        assertTrue(result.isFailure)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getReviewsByPerfumeId server error`() = runTest {
        coEvery { api.getReviewsByPerfumeId("p1") } returns Response.error(500, "".toResponseBody())

        val result = repository.getReviewsByPerfumeId("p1")

        assertTrue(result.isFailure)
        assertEquals("Error del servidor: 500", result.exceptionOrNull()?.message)
    }
    //endregion

    //region createReview Tests
    @Test
    fun `createReview success`() = runTest {
        val reviewDto = ReviewDto("p1", "c1", 5, "Great")
        val apiResponse = SingleReviewResponse(success = true, data = reviewDto, message = null)
        coEvery { api.createReview(any()) } returns Response.success(apiResponse)

        val result = repository.createReview("p1", "c1", 5, "Great")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `createReview api error`() = runTest {
        val reviewDto = ReviewDto("p1", "c1", 5, "Great")
        val apiResponse = SingleReviewResponse(success = false, data = reviewDto, message = "Creation failed")
        coEvery { api.createReview(any()) } returns Response.success(apiResponse)

        val result = repository.createReview("p1", "c1", 5, "Great")

        assertTrue(result.isFailure)
        assertEquals("Creation failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `createReview server error`() = runTest {
        coEvery { api.createReview(any()) } returns Response.error(400, "".toResponseBody())

        val result = repository.createReview("p1", "c1", 5, "Great")

        assertTrue(result.isFailure)
    }

    @Test
    fun `createReview throws exception`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { api.createReview(any()) } throws exception

        val result = repository.createReview("p1", "c1", 5, "Great")

        assertTrue(result.isFailure)
        assertEquals("Error de conexión: ${exception.message}", result.exceptionOrNull()?.message)
    }
    //endregion
}
