package com.example.perfulandia.viewmodel

import com.example.perfulandia.model.Perfume
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartViewModelTest {

    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        viewModel = CartViewModel()
    }

    @Test
    fun `add new perfume to cart`() {
        val perfume = Perfume("1", "Light Blue", "D&G", "Citrus", 100, "Unisex", 95.5, 10, "cat1", "desc", "img")
        viewModel.addToCart(perfume)

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals(perfume, state.items[0].perfume)
        assertEquals(1, state.items[0].quantity)
        assertEquals(95.5, state.total, 0.01)
    }

    @Test
    fun `add existing perfume to cart`() {
        val perfume = Perfume("1", "Light Blue", "D&G", "Citrus", 100, "Unisex", 95.5, 10, "cat1", "desc", "img")
        viewModel.addToCart(perfume)
        viewModel.addToCart(perfume)

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals(2, state.items[0].quantity)
        assertEquals(191.0, state.total, 0.01)
    }

    @Test
    fun `remove one unit from cart`() {
        val perfume = Perfume("1", "Light Blue", "D&G", "Citrus", 100, "Unisex", 95.5, 10, "cat1", "desc", "img")
        viewModel.addToCart(perfume)
        viewModel.addToCart(perfume)
        viewModel.removeFromCart(perfume)

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals(1, state.items[0].quantity)
        assertEquals(95.5, state.total, 0.01)
    }

    @Test
    fun `remove last unit from cart`() {
        val perfume = Perfume("1", "Light Blue", "D&G", "Citrus", 100, "Unisex", 95.5, 10, "cat1", "desc", "img")
        viewModel.addToCart(perfume)
        viewModel.removeFromCart(perfume)

        val state = viewModel.uiState.value
        assertEquals(0, state.items.size)
        assertEquals(0.0, state.total, 0.01)
    }

    @Test
    fun `clear cart`() {
        val perfume1 = Perfume("1", "Light Blue", "D&G", "Citrus", 100, "Unisex", 95.5, 10, "cat1", "desc", "img")
        val perfume2 = Perfume("2", "Sauvage", "Dior", "Spicy", 100, "Male", 120.0, 5, "cat2", "desc2", "img2")
        viewModel.addToCart(perfume1)
        viewModel.addToCart(perfume2)
        viewModel.clearCart()

        val state = viewModel.uiState.value
        assertEquals(0, state.items.size)
        assertEquals(0.0, state.total, 0.01)
    }
}