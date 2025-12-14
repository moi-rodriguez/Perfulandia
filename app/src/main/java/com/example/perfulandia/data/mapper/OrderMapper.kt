package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.OrderDto
import com.example.perfulandia.model.Order
import com.example.perfulandia.model.Perfume
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object OrderMapper {

    // NOTE: This is a partial mapping. The API returns perfume IDs, but the domain model
    // expects full Perfume objects. A full implementation would require fetching perfume
    // details for each item, which is out of scope for a simple mapper.
    // We will simulate it by creating dummy Perfume objects.
    fun fromDto(dto: OrderDto): Order {
        val perfumeStubs = dto.items.map {
            Perfume(
                id = it.perfume,
                nombre = "Cargando...", // Placeholder name
                precio = it.precio
            )
        }
        return Order(
            id = "order_${System.currentTimeMillis()}", // The DTO doesn't have an ID, creating a temporary one
            fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()), // DTO has no date
            total = dto.total,
            perfumes = perfumeStubs
        )
    }

    fun fromDtoList(dtoList: List<OrderDto>): List<Order> {
        return dtoList.map { fromDto(it) }
    }
}