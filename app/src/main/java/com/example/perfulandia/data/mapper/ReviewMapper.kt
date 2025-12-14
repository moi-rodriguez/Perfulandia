package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.ReviewDto
import com.example.perfulandia.model.Review

object ReviewMapper {

    fun fromDto(dto: ReviewDto): Review {
        return Review(
            id = "review_${dto.cliente}_${dto.perfume}", // DTO has no ID, creating a composite one
            calificacion = dto.puntuacion,
            comentario = dto.comentario,
            autor = dto.cliente // Assuming the 'cliente' field is the author's name or ID
        )
    }

    fun fromDtoList(dtoList: List<ReviewDto>): List<Review> {
        return dtoList.map { fromDto(it) }
    }
}