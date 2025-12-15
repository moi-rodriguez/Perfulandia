package com.example.perfulandia.repository

import com.example.perfulandia.data.remote.api.PerfumeApi
import com.example.perfulandia.data.remote.api.SinglePerfumeResponse
import com.example.perfulandia.data.remote.dto.PerfumeDto
import com.example.perfulandia.data.remote.dto.PerfumesResponse
import com.example.perfulandia.data.repository.PerfumeRepository
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
class PerfumeRepositoryTest {

    private lateinit var repository: PerfumeRepository
    private lateinit var api: PerfumeApi

    @Before
    fun setUp() {
        api = mockk()
        repository = PerfumeRepository(api)
    }

    @Test
    fun `getAllPerfumes success`() = runTest {
        val perfumeDtos = listOf(
            PerfumeDto(_id = "1", nombre = "Cool Water", precio = 50.0),
            PerfumeDto(_id = "2", nombre = "Acqua di Gio", precio = 75.0)
        )
        val apiResponse = PerfumesResponse(success = true, data = perfumeDtos, message = null)
        coEvery { api.getAllPerfumes() } returns Response.success(apiResponse)

        val result = repository.getAllPerfumes()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Cool Water", result.getOrNull()?.get(0)?.nombre)
    }

    @Test
    fun `getAllPerfumes api error`() = runTest {
        val apiResponse = PerfumesResponse(success = false, data = emptyList(), message = "Database error")
        coEvery { api.getAllPerfumes() } returns Response.success(apiResponse)

        val result = repository.getAllPerfumes()

        assertTrue(result.isFailure)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAllPerfumes server error`() = runTest {
        coEvery { api.getAllPerfumes() } returns Response.error(500, "".toResponseBody())

        val result = repository.getAllPerfumes()

        assertTrue(result.isFailure)
        assertEquals("Error del servidor: 500", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getPerfumeById success`() = runTest {
        val perfumeDto = PerfumeDto(_id = "1", nombre = "Cool Water", precio = 50.0)
        val apiResponse = SinglePerfumeResponse(success = true, data = perfumeDto, message = null)
        coEvery { api.getPerfumeById("1") } returns Response.success(apiResponse)

        val result = repository.getPerfumeById("1")

        assertTrue(result.isSuccess)
        assertEquals("Cool Water", result.getOrNull()?.nombre)
    }

    @Test
    fun `getPerfumeById not found`() = runTest {
        val apiResponse = SinglePerfumeResponse(success = false, data = null, message = "Perfume not found")
        coEvery { api.getPerfumeById("1") } returns Response.success(apiResponse)

        val result = repository.getPerfumeById("1")

        assertTrue(result.isFailure)
        assertEquals("Perfume not found", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `createPerfume success`() = runTest {
        val perfumeToCreate = com.example.perfulandia.model.Perfume("","Test","Test","Test",100, "U", 1.0, 1,"1","t","t","t")
        val createdPerfumeDto = PerfumeDto(_id = "new-id", nombre = "Test")
        val apiResponse = SinglePerfumeResponse(success = true, data = createdPerfumeDto, message = null)
        coEvery { api.createPerfume(any()) } returns Response.success(apiResponse)

        val result = repository.createPerfume(perfumeToCreate)

        assertTrue(result.isSuccess)
        assertEquals("new-id", result.getOrNull()?.id)
        assertEquals("Test", result.getOrNull()?.nombre)
    }
}
