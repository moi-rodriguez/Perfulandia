package com.example.perfulandia.repository

import com.example.perfulandia.data.remote.api.CategoryApi
import com.example.perfulandia.data.remote.dto.CategoriesResponse
import com.example.perfulandia.data.remote.dto.CategoryDto
import com.example.perfulandia.data.repository.CategoryRepository
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
class CategoryRepositoryTest {

    private lateinit var repository: CategoryRepository
    private lateinit var api: CategoryApi

    @Before
    fun setUp() {
        api = mockk()
        repository = CategoryRepository(api)
    }

    @Test
    fun `getAllCategories success`() = runTest {
        val categoryDtos = listOf(
            CategoryDto(id = "1", nombre = "Citrus"),
            CategoryDto(id = "2", nombre = "Woody")
        )
        val apiResponse = CategoriesResponse(success = true, data = categoryDtos)
        coEvery { api.getAllCategories() } returns Response.success(apiResponse)

        val result = repository.getAllCategories()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Citrus", result.getOrNull()?.get(0)?.nombre)
    }

    @Test
    fun `getAllCategories api error`() = runTest {
        val apiResponse = CategoriesResponse(success = false, data = emptyList(), message = "API error")
        coEvery { api.getAllCategories() } returns Response.success(apiResponse)

        val result = repository.getAllCategories()

        assertTrue(result.isFailure)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAllCategories server error`() = runTest {
        coEvery { api.getAllCategories() } returns Response.error(500, "".toResponseBody())

        val result = repository.getAllCategories()

        assertTrue(result.isFailure)
        assertEquals("Error del servidor: 500", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAllCategories throws exception`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { api.getAllCategories() } throws exception

        val result = repository.getAllCategories()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
