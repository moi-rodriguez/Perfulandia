package com.example.perfulandia.repository

import com.example.perfulandia.data.remote.api.OrderApi
import com.example.perfulandia.data.remote.api.OrderListResponse
import com.example.perfulandia.data.remote.api.SingleOrderResponse
import com.example.perfulandia.data.remote.dto.OrderDto
import com.example.perfulandia.data.remote.dto.OrderItemDto
import com.example.perfulandia.data.repository.OrderRepository
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
class OrderRepositoryTest {

    private lateinit var repository: OrderRepository
    private lateinit var api: OrderApi

    @Before
    fun setUp() {
        api = mockk()
        repository = OrderRepository(api)
    }

    //region getMyOrders Tests
    @Test
    fun `getMyOrders success`() = runTest {
        val orderDto = OrderDto("client1", listOf(OrderItemDto("p1", 1, 10.0)), 10.0)
        val apiResponse = OrderListResponse(success = true, data = listOf(orderDto), message = null)
        coEvery { api.getMyOrders() } returns Response.success(apiResponse)

        val result = repository.getMyOrders()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `getMyOrders api error`() = runTest {
        val apiResponse = OrderListResponse(success = false, data = emptyList(), message = "API error")
        coEvery { api.getMyOrders() } returns Response.success(apiResponse)

        val result = repository.getMyOrders()

        assertTrue(result.isFailure)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getMyOrders server error`() = runTest {
        coEvery { api.getMyOrders() } returns Response.error(500, "".toResponseBody())

        val result = repository.getMyOrders()

        assertTrue(result.isFailure)
        assertEquals("Error del servidor: 500", result.exceptionOrNull()?.message)
    }
    //endregion

    //region checkout Tests
    @Test
    fun `checkout success`() = runTest {
        val items = listOf(OrderItemDto("p1", 2, 25.0))
        val orderDto = OrderDto("client1", items, 50.0)
        val apiResponse = SingleOrderResponse(success = true, data = orderDto, message = "Success")
        coEvery { api.createOrder(any()) } returns Response.success(apiResponse)

        val result = repository.checkout("client1", items, 50.0)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `checkout api error`() = runTest {
        val items = listOf(OrderItemDto("p1", 2, 25.0))
        val orderDto = OrderDto("client1", items, 50.0)
        val apiResponse = SingleOrderResponse(success = false, data = orderDto, message = "Checkout failed")
        coEvery { api.createOrder(any()) } returns Response.success(apiResponse)

        val result = repository.checkout("client1", items, 50.0)

        assertTrue(result.isFailure)
        assertEquals("Checkout failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `checkout server error`() = runTest {
        val items = listOf(OrderItemDto("p1", 2, 25.0))
        coEvery { api.createOrder(any()) } returns Response.error(400, "".toResponseBody())

        val result = repository.checkout("client1", items, 50.0)

        assertTrue(result.isFailure)
    }

    @Test
    fun `checkout throws exception`() = runTest {
        val items = listOf(OrderItemDto("p1", 2, 25.0))
        val exception = RuntimeException("Network error")
        coEvery { api.createOrder(any()) } throws exception

        val result = repository.checkout("client1", items, 50.0)

        assertTrue(result.isFailure)
        assertEquals("Error de conexión: ${exception.message}", result.exceptionOrNull()?.message)
    }
    //endregion
}
