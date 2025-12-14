package com.example.perfulandia.data.mapper

import com.example.perfulandia.data.remote.dto.CategoryDto
import com.example.perfulandia.model.Category

object CategoryMapper {

    fun fromDto(dto: CategoryDto): Category {
        return Category(
            id = dto.id,
            nombre = dto.nombre,
            imagen = dto.imagen
        )
    }

    fun fromDtoList(dtoList: List<CategoryDto>): List<Category> {
        return dtoList.map { fromDto(it) }
    }
}