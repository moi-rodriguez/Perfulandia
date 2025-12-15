package com.example.perfulandia.viewmodel

import com.example.perfulandia.data.repository.OrderRepository
import com.example.perfulandia.model.Order
import com.example.perfulandia.model.Perfume
import io.mockk.coEvery
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
class OrderViewModelTest {

    private lateinit var viewModel: OrderViewModel
    private lateinit var orderRepository: OrderRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        orderRepository = mockk()
        viewModel = OrderViewModel(orderRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMyOrders success`() = runTest {
        val perfume = Perfume("1", "Perfume Name", "Brand", "Fragrance", 100, "Unisex", 99.99, 10, "1", "catNombre", "Description", "image_url", "thumb_url")
        val orders = listOf(
            Order(id = "o1", fecha = "2025-12-15", total = 99.99, perfumes = listOf(perfume))
        )
        coEvery { orderRepository.getMyOrders() } returns Result.success(orders)

        viewModel.loadMyOrders()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(orders, state.orders)
        assertNull(state.error)
    }

    @Test
    fun `loadMyOrders failure`() = runTest {
        val errorMessage = "Failed to load orders"
        coEvery { orderRepository.getMyOrders() } returns Result.failure(Exception(errorMessage))

        viewModel.loadMyOrders()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.orders.isEmpty())
        assertNotNull(state.error)
        assertEquals(errorMessage, state.error)
    }
}
